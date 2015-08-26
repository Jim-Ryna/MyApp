package com.example.jim.myapplication.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by jim on 2015/8/10.
 */
public class MorePic extends BmobObject{
    private MyInfo user;
    private BmobFile picFile;
    private String picUri;

    public MorePic() {
    }

    public MorePic(BmobFile picFile, MyInfo user, String picUri) {
        this.picFile = picFile;
        this.user = user;
        this.picUri = picUri;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public MorePic(BmobFile picFile) {
        this.picFile = picFile;
    }

    public BmobFile getPicFile() {
        return picFile;
    }

    public void setPicFile(BmobFile picFile) {
        this.picFile = picFile;
    }

    public MyInfo getUser() {
        return user;
    }

    public void setUser(MyInfo user) {
        this.user = user;
    }
}
