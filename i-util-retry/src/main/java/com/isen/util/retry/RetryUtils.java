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
 * @author Isen
 * @date 2018/11/1 16:16
 * @since 1.0
 */
public enum  RetryUtils{
    /**
     * 实例
     */
    INSTANCE;

    private RetryInfoMapper retryInfoMapper;

    private Gson gson = new Gson();

    public static RetryUtils instance(){
        return INSTANCE;
    }

    public void init(RetryInfoMapper retryInfoMapper){
        this.retryInfoMapper = retryInfoMapper;
    }

    /**
     * 调用 object.methodName(parameters)
     * @param object 执行调用的对象
     * @param methodName 调用的方法名
     * @param parameters 参数列表
     * @param <T> 返回值类型
     * @return 返回执行结果
     */
    private <T> T retry(Object object, String methodName, Object[] parameters, Class[] paramTypes) throws Exception {
        validParameters(object, methodName);

        Class<?> clazz = object.getClass();
        Method targetMethod = clazz.getMethod(methodName, paramTypes);
        return (T)targetMethod.invoke(object, parameters);
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
        Object[] parameters = {};
        Class[] parameterTypes = {};
        String[] parameterTypeStrs = {};
        for(RetryInfo retryInfo : retryInfos){
            //解析参数列表
            if(retryInfo.getParameters() != null){
                // FIXME isen  会将 int 解析成 double,如果存储的是更为复杂的类型呢？考虑序列化
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
//            try {
//                for (int i = 0; i < parameters.length; i++) {
//                    parameters[i] = parameterTypes[i].cast(parameters[i]);
//                }
//            }catch (Exception e){
//                //转型失败
//                e.printStackTrace();
//                continue;
//            }

            try {
                //进行重试
                Method targetMethod = clazz.getMethod(methodName, parameterTypes);
                Object re = targetMethod.invoke(object, parameters);

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
     * 先保存调用记录，然后调用 object.methodName(parameters)，要求parameters的类型不能是基础类型。
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
            retryInfo.setParameters(gson.toJson(parameters));
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
