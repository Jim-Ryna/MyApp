package com.example.jim.myapplication.model;

/**
 * Created by jim on 2015/8/8.
 */
public class AllClassMate {
    public AllClassMate() {
    }

    public AllClassMate(String headPic, String name, Boolean phoneShow, String signature, MyInfo user) {
        this.headPic = headPic;
        this.name = name;
        this.phoneShow = phoneShow;
        this.signature = signature;
        this.user = user;
    }

    private MyInfo user;
    private String headPic;
    private String name;
    private Boolean phoneShow;
    private String signature;

    public MyInfo getUser() {
        return user;
    }

    public void setUser(MyInfo user) {
        this.user = user;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Boolean getPhoneShow() {
        return phoneShow;
    }

    public void setPhoneShow(Boolean phoneShow) {
        this.phoneShow = phoneShow;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
