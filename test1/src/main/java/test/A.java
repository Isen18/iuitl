package test;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Isen
 * @date 2018/12/16 10:32
 * @since 1.0
 */
public class A implements Serializable {

    private static final long serialVersionUID = 3555812322769894301L;
    private int a = 10;
    private Map<String, String> properties;

    public A(int a, Map<String, String> properties) {
        this.a = a;
        this.properties = properties;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "A{" +
                "a=" + a +
                ", properties=" + properties +
                '}';
    }
}
