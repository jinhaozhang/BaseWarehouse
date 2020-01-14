package com.example.basewarehouse.app;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.context=this;
    }
}
