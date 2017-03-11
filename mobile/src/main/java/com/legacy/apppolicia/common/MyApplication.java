package com.legacy.apppolicia.common;

import android.app.Application;

import com.pushbots.push.Pushbots;

/**
 * Created by ivanl on 10/03/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Pushbots Library
        Pushbots.sharedInstance().init(this);
    }
}
