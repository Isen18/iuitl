package com.isen.util.retry;

import com.isen.util.retry.mapper.RetryInfoMapper;
import com.isen.util.retry.pojo.RetryInfo;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
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

//    /**
//     * Method: retry(Object object, String methodName, Object ...parameters)
//     */
//    @Test
//    public void testRetry() throws Exception {
//        String str = testObject.getHello("张三", 12);
//        System.out.println(str);
//
//        String str2 = RetryUtils.retry(testObject, "getHello", "张三", 12);
//        System.out.println(str2);
//
//        String str3 = RetryUtils.retry(testObject, "printHello");
//        System.out.println(str3);
//    }

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
} 
