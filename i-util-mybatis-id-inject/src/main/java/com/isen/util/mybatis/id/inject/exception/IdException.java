package com.isen.util.mybatis.id.inject.exception;

/**
 * @author Isen
 * @date 2018/12/28 16:06
 * @since 1.0
 */
public class IdException extends RuntimeException {
    public IdException(String message) {
        super(message);
    }

    public IdException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdException(Throwable cause) {
        super(cause);
    }
}
