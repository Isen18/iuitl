package com.isen.util.retry;

import com.google.gson.Gson;
import com.isen.util.retry.mapper.RetryInfoMapper;
import com.isen.util.retry.pojo.RetryInfo;
import com.isen.util.retry.pojo.RetryInfoExample;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 重试工具类
 *
 * // TODO isen 2018/12/12 暂时无法处理泛型
 * // TODO isen 2018/12/12 方法的选择无法同java的重载规则一致
 *
 * @author Isen
 * @date 2018/11/1 16:16
 * @since 1.0
 */
public enum RetryUtils{
    /**
     * 实例
     */
    INSTANCE;

    private RetryInfoMapper retryInfoMapper;

    private Gson gson = new Gson();

    public void init(RetryInfoMapper retryInfoMapper){
        this.retryInfoMapper = retryInfoMapper;
    }

    /**
     * 调用 object.methodName(parameters)
     * @param object 执行调用的对象
     * @param methodName 调用的方法名
     * @param parameters 参数列表
     * @param paramTypes 参数类型
     * @param <T> 返回值类型
     * @return 返回执行结果
     */
    private <T> T retry(Object object, String methodName, Object[] parameters, Class[] paramTypes) throws Exception {
        validParameters(object, methodName);

        Class<?> clazz = object.getClass();
        Method targetMethod = getMethod(methodName, paramTypes, clazz);
        return (T)targetMethod.invoke(object, parameters);
    }

    private Method getMethod(String methodName, Class[] paramTypes, Class<?> clazz)
            throws NoSuchMethodException {
        Method targetMethod = null;
        if(paramTypes == null || paramTypes.length == 0){
            //1、没有参数
            targetMethod = clazz.getMethod(methodName);
        }else {
            //2、有参数
            //如果paramTypes中含有基本类型的包装类，而methodName的参数类型为基本类型，将无法获取到相应的方法
//        Method targetMethod = clazz.getMethod(methodName, paramTypes);
            for (Method method : clazz.getMethods()) {
                if(method.getName().equals(methodName) && equals(method.getParameterTypes(), paramTypes)){
                    targetMethod = method;
                    break;
                }
            }
        }

        if(targetMethod == null){
            throw new NoSuchMethodException();
        }
        return targetMethod;
    }

    private boolean equals(Class<?>[] declare, Class<?>[] real){
        if(declare == null && real == null){
            return true;
        }

        if(declare == null || real == null){
            return false;
        }

        if(declare.length != real.length){
            return false;
        }

        for (int i = 0; i < declare.length; i++) {
            if(!equalMethodName(declare[i], real[i])
                    && declare[i] != real[i]
                    && !declare[i].isAssignableFrom(real[i])){
                return false;
            }
        }

        return true;
    }

    private boolean equalMethodName(Class<?> declare, Class<?> real){
        String declareName = convert(declare.getName());
        String realName = convert(real.getName());
        return declareName.equals(realName);
    }

    private String convert(String name){
        if (name.equals("java.lang.Byte")){
            return "byte";
        }

        if (name.equals("java.lang.Short")){
            return "short";
        }

        if(name.equals("java.lang.Integer")){
            return "int";
        }

        if(name.equals("java.lang.Long")){
            return "long";
        }

        if(name.equals("java.lang.Float")){
            return "float";
        }

        if(name.equals("java.lang.Double")){
            return "double";
        }

        if(name.equals("java.lang.Boolean")){
            return "boolean";
        }

        if(name.equals("java.lang.Char")){
            return "char";
        }

        return name;
    }

    /**
     * 先获取调用失败的记录，然后进行重试
     * @param object 执行调用的对象
     * @param methodName 调用的方法名
     * @return 重试结果
     */
    public List<RetryResult> stashPopAndRetry(Object object, String methodName){
        validParameters(object, methodName);
        Class clazz = object.getClass();
        List<RetryResult> result = new ArrayList<>();

        //获取调用失败的记录
        List<RetryInfo> retryInfos = getInvokeFailRecord(clazz.getName(), methodName);
        if(retryInfos == null || retryInfos.isEmpty()){
            return result;
        }

        //根据每个失败的调用记录进行重新调用
        String[] parameters = {};
        Class[] parameterTypes = {};
        String[] parameterTypeStrs = {};
        Object[] params = {};
        for(RetryInfo retryInfo : retryInfos){
            //解析参数列表
            if(retryInfo.getParameters() != null){
                parameters = gson.fromJson(retryInfo.getParameters(), parameters.getClass());
            }

            //解析参数类型列表
            if(retryInfo.getParameterTypes() != null){
                parameterTypeStrs = gson.fromJson(retryInfo.getParameterTypes(), parameterTypeStrs.getClass());
                parameterTypes = new Class[parameterTypeStrs.length];
                for (int i = 0; i < parameterTypeStrs.length; i++) {
                    try {
                        parameterTypes[i] = Class.forName(parameterTypeStrs[i]);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            //对参数列表进行参数转型(String=>真正类型)
            params = new Object[parameters.length];
            try {
                for (int i = 0; i < parameters.length; i++) {
                    params[i] = gson.fromJson(parameters[i], parameterTypes[i]);
                }
            }catch (Exception e){
                //转型失败
                e.printStackTrace();
                continue;
            }

            try {
                //进行重试
                Method targetMethod = getMethod(methodName, parameterTypes, clazz);
//                Method targetMethod = clazz.getMethod(methodName, parameterTypes);
                Object re = targetMethod.invoke(object, params);

                //重试成功
                RetryResult retryResult = new RetryResult();
                retryResult.setRetryReturn(re);
                retryResult.setParameters(parameters);
                result.add(retryResult);

                //更新调用记录
                updateInvokeStatus(retryInfo.getId(), InvokeStatusEn.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 先保存调用记录，然后调用 object.methodName(parameters)
     * @param object 执行调用的对象
     * @param methodName 调用的方法名
     * @param parameters 参数列表
     * @param <T> 返回值类型
     * @return 返回执行结果
     */
    public <T> T stashAndInvoke(Object object, String methodName, Object ...parameters)
            throws Exception {
        Class[] paramTypes = getClass(parameters);
        Long id = saveInvokeRecord(object, methodName, parameters, paramTypes);
        T result = retry(object, methodName, parameters, paramTypes);
        updateInvokeStatus(id, InvokeStatusEn.SUCCESS);
        return result;
    }

    /**
     * 保存调用记录
     * @param object 执行调用的对象
     * @param methodName 调用的方法名
     * @param parameters 参数列表
     * @param paramTypes 参数类型列表
     * @return 调用记录id
     */
    private Long saveInvokeRecord(Object object, String methodName, Object[] parameters,  Class[] paramTypes){
        validParameters(object, methodName);
        Class clazz = object.getClass();
        String className = clazz.getName();

        RetryInfo retryInfo = new RetryInfo();
        retryInfo.setClassName(className);
        retryInfo.setMethodName(methodName);

        if(parameters != null && parameters.length > 0){
            //将所有参数均转换成字符串
            String[] paramStrs = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                paramStrs[i] = gson.toJson(parameters[i]);
            }
            retryInfo.setParameters(gson.toJson(paramStrs));
        }

        if(paramTypes != null && paramTypes.length > 0){
            String[] paramTypeStrs = new String[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                paramTypeStrs[i] = paramTypes[i].getName();
            }
            retryInfo.setParameterTypes(gson.toJson(paramTypeStrs));
        }

        retryInfoMapper.insertSelective(retryInfo);
        return retryInfo.getId();
    }

    /**
     * 获取调用失败的记录
     * @param className 执行调用的对象类名
     * @param methodName 调用的方法名
     * @return
     */
    private List<RetryInfo> getInvokeFailRecord(String className, String methodName){
        RetryInfoExample retryInfoExample = new RetryInfoExample();
        retryInfoExample.createCriteria().andClassNameEqualTo(className).andMethodNameEqualTo(methodName).andStausEqualTo(InvokeStatusEn.FALL.getCode());
        return retryInfoMapper.selectByExample(retryInfoExample);
    }

    /**
     * 更新调用记录调用状态
     * @param id 记录id
     * @param invokeStatusEn 状态
     */
    private void updateInvokeStatus(Long id, InvokeStatusEn invokeStatusEn){
        RetryInfo retryInfo = new RetryInfo();
        retryInfo.setId(id);
        retryInfo.setStaus(invokeStatusEn.getCode());
        retryInfoMapper.updateByPrimaryKeySelective(retryInfo);
    }

    /**
     * 获取对象的类名
     * @param parameters 对象
     * @return 类型，null，如果 parameters 为空
     */
    private Class[] getClass(Object []parameters){
        Class[] result = null;
        if(parameters == null || parameters.length == 0){
            return result;
        }

        result = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            result[i] = parameters[i].getClass();
        }
        return result;
    }

    private void validParameters(Object object, String methodName){
        if(object == null){
            throw new NullPointerException("object == null");
        }

        if(methodName == null){
            throw new NullPointerException("methodName == null");
        }
    }
}
