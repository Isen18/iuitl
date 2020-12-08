package com.isen.util.route.datasource.exception;

import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Isen
 * @date 2019/4/30 0:08
 * @since 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 用来处理bean validation异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public String resolveConstraintViolationException(ConstraintViolationException ex) {
        System.out.println("============================" + ex.getMessage());
        for (ConstraintViolation<?> s : ex.getConstraintViolations()) {
            return s.getInvalidValue() + ": " + s.getMessage();
        }
        return "参数非法";
    }

    /**
     * 用来处理方法参数验 validation异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public String resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        System.out.println("============================" + ex.getMessage());
        return "参数校验失败";
    }
}
