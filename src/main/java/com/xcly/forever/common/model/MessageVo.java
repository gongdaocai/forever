package com.xcly.forever.common.model;

/**
 * @Description: 消息
 * @Author: gdc
 * @Date: 2019-08-03 14:54
 **/
public class MessageVo {
    /**
     * 子订单集合详细
     */
    private String appSubOrderLstDetail;

    /**
     * 订单商品集合详细
     */
    private String appOrderGoodsLstDetail;

    /**
     * 订单地址详细
     */
    private String appOrderAddressDetail;

    private String userid;

    public String getAppSubOrderLstDetail() {
        return appSubOrderLstDetail;
    }

    public void setAppSubOrderLstDetail(String appSubOrderLstDetail) {
        this.appSubOrderLstDetail = appSubOrderLstDetail;
    }

    public String getAppOrderGoodsLstDetail() {
        return appOrderGoodsLstDetail;
    }

    public void setAppOrderGoodsLstDetail(String appOrderGoodsLstDetail) {
        this.appOrderGoodsLstDetail = appOrderGoodsLstDetail;
    }

    public String getAppOrderAddressDetail() {
        return appOrderAddressDetail;
    }

    public void setAppOrderAddressDetail(String appOrderAddressDetail) {
        this.appOrderAddressDetail = appOrderAddressDetail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
    