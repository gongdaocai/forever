package com.xcly.forever.common.result;

/**
 * 异常返回体
 */
public class BizResultVO {
    /**
     * 业务码
     */
    private Integer bizCode;

    /**
     * 业务消息
     */
    private String bizMessage;

    /**
     * 业务数据体
     */
    private Object bizData;

    public Integer getBizCode() {
        return bizCode;
    }

    public void setBizCode(Integer bizCode) {
        this.bizCode = bizCode;
    }

    public String getBizMessage() {
        return bizMessage;
    }

    public void setBizMessage(String bizMessage) {
        this.bizMessage = bizMessage;
    }

    public Object getBizData() {
        return bizData;
    }

    public void setBizData(Object bizData) {
        this.bizData = bizData;
    }

    public BizResultVO() {
    }

    public BizResultVO(Integer bizCode, String bizMessage, Object bizData) {
        this.bizCode = bizCode;
        this.bizMessage = bizMessage;
        this.bizData = bizData;
    }

    @Override
    public String toString() {
        return "BizResultVO{" +
                "bizCode=" + bizCode +
                ", bizMessage='" + bizMessage + '\'' +
                ", bizData=" + bizData +
                '}';
    }
}
