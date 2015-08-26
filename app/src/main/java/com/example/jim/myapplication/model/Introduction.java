package com.example.jim.myapplication.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by jim on 2015/8/10.
 */
public class Introduction extends BmobObject{
    private MyInfo user;
    private String signature;
    private String introduce;
    private boolean phoneShow;
    private String QQ;
    private String weixin;
    private BmobFile backImage;
    private String picUri;

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public BmobFile getBackImage() {
        return backImage;
    }

    public void setBackImage(BmobFile backImage) {
        this.backImage = backImage;
    }

    public Introduction(){}
    public MyInfo getUser() {
        return user;
    }

    public void setUser(MyInfo user) {
        this.user = user;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public boolean isPhoneShow() {
        return phoneShow;
    }

    public void setPhoneShow(boolean phoneShow) {
        this.phoneShow = phoneShow;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
}
