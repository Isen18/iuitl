package com.isen.util.route.datasource.controller;

import com.isen.util.route.datasource.vo.UpdateGroup;
import com.isen.util.route.datasource.vo.UserVo;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Isen
 * @date 2019/4/29 23:03
 * @since 1.0
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    /**
     * curl http://localhost:8080/user/add
     * @param userVo
     * @return
     */
    @RequestMapping("/add")
    public String addUser(@Valid UserVo userVo, BindingResult bindingResult){
        //BindingResult 一定要直接紧跟在要校验的参数后面，每个要校验的参数后边都可以有一个 BindingResult
        //BindingResult 主要是获取错误信息，非必须，因为如果校验失败，会抛出异常，有统一异常处理器处理

        if (bindingResult.hasErrors()) {
            List<ObjectError> objectErrors = bindingResult.getAllErrors();
            for(ObjectError objectError : objectErrors){
                if (objectError instanceof FieldError) {
                    FieldError fieldError = (FieldError) objectError;
                    System.out.println(fieldError.getField() + fieldError.getDefaultMessage());
                }
                //进行一些出错处理，例如跳转到错误页面
            }
        }
        return "ok";
    }

    @RequestMapping("/add2")
    public String addUser(@NotBlank(message = "姓名不能为空") String name, @Range(min = 1, max = 150, message = "年龄需要在{min}-{max}之间") @NotNull String age){
        //BindingResult省略

        return "ok";
    }

    @RequestMapping("/update")
    public String updateUser(@Validated({UpdateGroup.class}) UserVo userVo, @Validated UserVo userVo2){
        //userVo 配置了分组 UpdateGroup，只使用具有标识分组 UpdateGroup 的校验规则
        //userVo2 没有配置分组，使用没有分组的校验规则
        return "ok";
    }
}
