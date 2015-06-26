package com.example.jim.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jim.myapplication.Config.App;
import com.example.jim.myapplication.model.MyInfo;
import com.example.jim.myapplication.utils.LogUtil;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jim on 2015/6/13.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    Spinner academy_spin;
    EditText classText;
    EditText nameText;
    EditText emailText;
    EditText phoneText;
    EditText passwordText;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initWidget();
        setacdemySpin();
        mButton.setOnClickListener(this);
    }
    private void initWidget(){
        emailText = (EditText) findViewById(R.id.email_text);
        academy_spin = (Spinner) findViewById(R.id.academy_spin);
        classText = (EditText) findViewById(R.id.class_text);
        nameText = (EditText) findViewById(R.id.name_Text);
        passwordText = (EditText) findViewById(R.id.password_text);
        phoneText = (EditText) findViewById(R.id.phone_text);
        mButton = (Button) findViewById(R.id.button);
    }
    private void setacdemySpin() {
        String[] academies = getResources().getStringArray(R.array.academy);
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, academies);
        academy_spin.setAdapter(_Adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                MyInfo info = new MyInfo();
                info.setUsername(nameText.getText().toString());
                info.setPassword(String.valueOf(passwordText.getText()));
                info.setMobilePhoneNumber(String.valueOf(phoneText.getText()));
                info.setEmail(emailText.getText().toString());
                info.signUp(RegisterActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        LogUtil.d("smile", "success");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        LogUtil.e("smile", "fail" + s);
                    }
                });
            break;
        }
    }
}
