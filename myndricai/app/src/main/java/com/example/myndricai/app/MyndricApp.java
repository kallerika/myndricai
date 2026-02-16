package com.example.myndricai.app;

import android.app.Application;

import com.example.myndricai.di.ServiceLocator;

public class MyndricApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ServiceLocator.init(this);
    }
}
