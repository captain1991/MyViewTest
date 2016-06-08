package com.xiaodong.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import cn.smssdk.gui.SMSReceiver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.initSDK(this,"131bc1c8fe31c","6fe2b44d94eb570daf197c527a6283f2");
        setContentView(R.layout.activity_main);
    }

    public void getCode(View view){
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
// 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    Log.e("RESULT_COMPLETE","RESULT_COMPLETE");
                    if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        @SuppressWarnings("unchecked")
                        HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                        String country = (String) phoneMap.get("country");
                        String phone = (String) phoneMap.get("phone");
                    }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功

                    }
// 提交用户信息
//                    registerUser(country, phone);
                }
            }
        });
        registerPage.show(this);
    }
}
