package com.example.jim.myapplication.model;

import android.text.Editable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by jim on 2015/6/14.
 */
public class MyInfo extends BmobUser{
    private String myAcademy;
    private String myClass;

    public String getMyClass() {
        return myClass;
    }

    public void setMyClass(String myClass) {
        this.myClass = myClass;
    }

    public String getMyAcademy() {
        return myAcademy;
    }

    public void setMyAcademy(String myAcademy) {
        this.myAcademy = myAcademy;
    }
}
