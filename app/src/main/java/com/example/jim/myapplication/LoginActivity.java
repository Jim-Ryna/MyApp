package com.example.jim.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.jim.myapplication.utils.LogUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jim on 2015/6/14.
 */
public class LoginActivity extends AppCompatActivity{
    Button mButton;
    BmobUser bu2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        bu2 = new BmobUser();
        bu2.setUsername("lucky");
        bu2.setPassword("123456");
        mButton = (Button) findViewById(R.id.login);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bu2.login(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        LogUtil.d("Login", "success");
                        Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        LogUtil.e("Login", "error" + msg);
                    }
                });
            }
        });
    }
}
