package com.xiaodong.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_getCode;
    private Button bt_getPhone;
    private Button bt_submit;
    private EditText phone;
    private EditText code;
    TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.initSDK(this, "131bc1c8fe31c", "6fe2b44d94eb570daf197c527a6283f2");
        SMSSDK.registerEventHandler(handler);
        setContentView(R.layout.activity_regist);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String number = telephonyManager.getLine1Number();
        bt_getCode = (Button) findViewById(R.id.bt_getCode);
        bt_getCode.setOnClickListener(this);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_getPhone = (Button) findViewById(R.id.bt_getPhone);
        bt_getPhone.setOnClickListener(this);
        phone = (EditText) findViewById(R.id.et_phone);
        code = (EditText) findViewById(R.id.et_code);
        Log.e("phoneNomber>>>","号码"+number);
        if(number!=null&&!number.equals("")) {
            phone.setText(number);
        }
    }

    @Override
    public void onClick(View v) {
        String number = telephonyManager.getLine1Number();
        Log.e("phoneNomber2>>>","号码"+number);
        if (v.getId()==bt_getCode.getId()){
            String p = phone.getText().toString().trim();
            SMSSDK.getVerificationCode("86",p);
        }else if (v.getId() == bt_submit.getId()){
            String p = phone.getText().toString().trim();
            String c = code.getText().toString().trim();
            SMSSDK.submitVerificationCode("86",p,c);
        }else if(v.getId()==R.id.bt_getPhone){
            getPhone();
        }
    }

    public void panduan(){
        String IMSI = telephonyManager.getSubscriberId();
        String ProvidersName;
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
    }

    public void getPhone() {
        Uri uri = Uri.parse("content://sms/sent");
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String phone = cursor.getString(cursor.getColumnIndex("address"));
                String phone_center = cursor.getString(cursor.getColumnIndex("service_center"));
                Log.e("phone","电话号码"+phone);
            } while (cursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(handler);
    }

    private EventHandler handler = new EventHandler(){
        @Override
        public void afterEvent(int i, int i1, Object o) {
            if(i1 == SMSSDK.RESULT_COMPLETE){
                if (i == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Log.e("获取验证码","获取验证码");
                }else if (i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    Log.e("提交验证码","提交验证码");
                }
            }else {
                Log.e("chucuo","出错");
            }

        }
    };
}
