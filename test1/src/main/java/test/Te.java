package test;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Isen
 * @date 2018/12/16 10:24
 * @since 1.0
 */
public class Te {


    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("ok", "ha?");
        A a = new A(10, map);
        System.out.println(JSON.toJSONString(a));
        String json = JSON.toJSONString(a);
        A a2= JSON.parseObject(json, A.class);
        System.out.println(a2);
    }
}
