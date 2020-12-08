package com.isen.util.route.datasource.vo;

import java.io.Serializable;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 * @author Isen
 * @date 2019/4/29 23:04
 * @since 1.0
 */
public class UserVo implements Serializable {
    @NotBlank(message = "{user.id.blank.error}", groups = {UpdateGroup.class})
    private String id;

    @NotBlank(message = "姓名不能为空", groups = {CommonGroup.class, UpdateGroup.class})
    private String name;

    @NotNull
    @Range(min = 1, max = 150, message = "年龄需要在{min}-{max}之间")
    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
