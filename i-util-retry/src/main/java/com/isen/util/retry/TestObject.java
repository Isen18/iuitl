package com.isen.util.retry;

import java.util.List;

/**
 * @author Isen
 * @date 2018/11/1 16:10
 * @since 1.0
 */
public class TestObject {

    public void printHello(){
        System.out.println("hello");
    }

    public int echoAge(int age){
        System.out.println( "int " + age);
        return age;
    }

    public String getHello(String name, Integer age){
        return "hello " + "name=" + name + " age=" + age;
    }

    public void printPer(Person person){
        System.out.println(person.getA());
    }

    public List<Person> printPerson(List<Person> personList){
        System.out.println(personList);
        return personList;
    }
}

class Person{
    int a = 10;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "Person{" +
                "a=" + a +
                '}';
    }
}