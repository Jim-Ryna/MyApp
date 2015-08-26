package com.example.jim.myapplication.utils.BmobUtil;

import android.app.Activity;
import android.content.Context;

import com.example.jim.myapplication.app.App;
import com.example.jim.myapplication.utils.LogUtil;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jim on 2015/8/12.
 */
public class BmobTableUtil {

    public static void insertObj(Activity activity, BmobObject obj) {
        obj.save(activity, new SaveListener() {
            @Override
            public void onSuccess() {

                LogUtil.i("BmobUtil", "chenggong");
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.i("BmobUtil", "失败" + s);
            }
        });
    }

    public static void insertObj(Activity activity, BmobObject obj, final AddInCallBack addInCallBack){
        obj.save(activity, new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtil.i("BmobUtil", "chenggong");
                addInCallBack.doInSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.i("BmobUtil", "失败" + s);
                addInCallBack.doInFail();

            }
        });
    }
}
