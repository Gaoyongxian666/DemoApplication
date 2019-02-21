package com.example.administrator.demoapplication;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONException;

import java.io.IOException;

public class MyAPP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(MyAPP.this, SpeechConstant.APPID + "=5c60f6b9"); //初始化
    }
}
