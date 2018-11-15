package com.isen.util.retry;

/**
 * 重试结果
 *
 * @author Isen
 * @date 2018/11/1 22:38
 * @since 1.0
 */
public class RetryResult {

    /**
     * 重试返回值
     */
    private Object retryReturn;

    /**
     * 重试的参数列表
     */
    private Object[] parameters;

    public Object getRetryReturn() {
        return retryReturn;
    }

    public void setRetryReturn(Object retryReturn) {
        this.retryReturn = retryReturn;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
