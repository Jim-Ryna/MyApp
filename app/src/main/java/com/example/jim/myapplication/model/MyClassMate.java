package com.example.jim.myapplication.model;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by jim on 2015/8/2.
 */
public class MyClassMate {
    private String phoneNo;
    private String classmateName;
    private String imagUrl;

    public MyClassMate() {
    }

    public MyClassMate(String classmateName, String imagUrl, String phoneNo) {
        this.classmateName = classmateName;
        this.imagUrl = imagUrl;
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getClassmateName() {
        return classmateName;
    }

    public void setClassmateName(String classmateName) {
        this.classmateName = classmateName;
    }

    public String getImagUrl() {
        return imagUrl;
    }

    public void setImagUrl(String imagUrl) {
        this.imagUrl = imagUrl;
    }
}
