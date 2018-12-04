package com.isen.util.base;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * CollectionUtilTest Tester.
 *
 * @author <isen>
 * @version 1.0
 * @since <pre>12/04/2018</pre>
 */
public class CollectionUtilTest {

    List<Integer> ids;
    List<Person> persons;

    @Before
    public void before() throws Exception {
        ids = Arrays.asList(12, 15);
        persons = Arrays.asList(new Person(12), new Person(16));
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: join(List<T> list, String separator)
     */
    @Test
    public void testJoinForListSeparator() throws Exception {
        String str = CollectionUtil.join(ids, "|");
        System.out.println(str);
    }

    /**
     * Method: join(List<T> list, Consumer<T, R> consumer, String separator)
     */
    @Test
    public void testJoinForListConsumerSeparator() throws Exception {
        String str = CollectionUtil.join(ids, tmp -> tmp,"|");
        System.out.println(str);
        str = CollectionUtil.join(ids, null,"|");
        System.out.println(str);

        str = CollectionUtil.join(persons, Person::getAge,"|");
        System.out.println(str);
        str = CollectionUtil.join(persons, tmp -> tmp.getAge(),"|");
        System.out.println(str);

    }

    /**
     * Method: main(String[] args)
     */
    @Test
    public void testMain() throws Exception {
    }

    /**
     * Method: get(T t)
     */
    @Test
    public void testGet() throws Exception {
    }


}

class Person{
    private int age;

    public Person(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
