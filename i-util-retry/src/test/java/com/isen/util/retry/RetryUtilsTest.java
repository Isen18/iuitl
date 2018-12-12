package com.isen.util.retry;

import com.isen.util.retry.mapper.RetryInfoMapper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * RetryUtils Tester.
 *
 * @author <isen>
 * @version 1.0
 * @since <pre>11/01/2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
//@ActiveProfiles("test")
public class RetryUtilsTest {

    @Resource
    private RetryInfoMapper retryInfoMapper;

    private TestObject testObject;

    @Before
    public void before() throws Exception {
        testObject = new TestObject();
        RetryUtils.instance().init(retryInfoMapper);
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void tet() throws Exception {
        int re = (int)testObject.getClass().getMethod("echoAge", int.class).invoke(testObject, 12);
        System.out.println(re);
    }

    @Test
    public void testStashAndInvoke() throws Exception {
        String str = RetryUtils.instance().stashAndInvoke(testObject, "getHello", "张三", 12);
        System.out.println(str);

//        String str = (String) (testObject.getClass().getMethod("getHello", String.class, int.class).invoke(testObject,"张三", 12));
//        String str2 = (String) (testObject.getClass().getMethod("getHello", String.class, Integer.TYPE).invoke(testObject,"张三", 12));
    }

    @Test
    public void testStashPopAndRetry() throws Exception{
        List<RetryResult> retryResults =  RetryUtils.instance().stashPopAndRetry(testObject, "getHello");
        System.out.println(retryResults);
    }

    @Test
    public void testStashAndInvoke2() throws Exception {
        int re = RetryUtils.instance().stashAndInvoke(testObject, "echoAge", 12);
        System.out.println(re);
    }

    @Test
    public void testStashPopAndRetry2() throws Exception{
        List<RetryResult> retryResults =  RetryUtils.instance().stashPopAndRetry(testObject, "echoAge");
        System.out.println(retryResults);
    }

    @Test
    public void testStashAndInvoke3() throws Exception {
        RetryUtils.instance().stashAndInvoke(testObject, "printHello");
    }

    @Test
    public void testStashPopAndRetry3() throws Exception{
        List<RetryResult> retryResults =  RetryUtils.instance().stashPopAndRetry(testObject, "printHello");
        System.out.println(retryResults);
    }

    @Test
    public void testStashAndInvoke4() throws Exception {
        Person person = new Person();
        person.setA(18);
        RetryUtils.instance().stashAndInvoke(testObject, "printPer", person);
    }

    @Test
    public void testStashPopAndRetry4() throws Exception{
        List<RetryResult> retryResults =  RetryUtils.instance().stashPopAndRetry(testObject, "printPer");
        System.out.println(retryResults);
    }

    @Test
    public void testStashAndInvoke5() throws Exception {
        List<Person> personList = new ArrayList<>();
        Person person = new Person();
        person.setA(18);
        personList.add(person);
        RetryUtils.instance().stashAndInvoke(testObject, "printPerson", personList);
    }

    @Test
    public void testStashPopAndRetry5() throws Exception{
        List<RetryResult> retryResults =  RetryUtils.instance().stashPopAndRetry(testObject, "printPerson");
        System.out.println(retryResults);
    }

    public static void main(String[] args) {
//        System.out.println(int.class.getName());
//        try {
//            Class clazz = Class.forName(int.class.getName());
//
//            System.out.println(clazz);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

//        for(Method method : TestObject.class.getMethods()){
//            for (Class<?> aClass : method.getParameterTypes()) {
//                System.out.println(aClass.getName());
//            }
//        }
//        TestObject testObject = new TestObject();
//        testObject.echoAge(new Integer(10));

        Class cl = Integer.class;
        Class clz = int.class;
        if(cl.isAssignableFrom(clz) ){
            System.out.println("ok1");
        }

        if(Number.class.isAssignableFrom(cl) ){
            System.out.println("ok2");
        }

        if(Number.class.isAssignableFrom(clz) ){
            System.out.println("ok3");
        }

        System.out.println(cl.getName());
        System.out.println(cl.getTypeName());
        System.out.println(clz.getName());
    }
}
