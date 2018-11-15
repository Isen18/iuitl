package com.isen.util.retry;

/**
 * 调用状态
 *
 * @author Isen
 * @date 2018/11/1 22:37
 * @since 1.0
 */
public enum InvokeStatusEn {
    /**
     * 失败
     */
    FALL(0),
    /**
     * 成功
     */
    SUCCESS(1);

    private byte code;
    InvokeStatusEn(int code){
        this.code = (byte) code;
    }

    public byte getCode(){
        return code;
    }

    public InvokeStatusEn of(Byte code){
        if(code == null){
            return null;
        }
        for(InvokeStatusEn invokeStatusEn : values()){
            if(invokeStatusEn.getCode() == code){
                return invokeStatusEn;
            }
        }
        return null;
    }
}
