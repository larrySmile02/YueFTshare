package com.lee.yueftshare;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.twitter.sdk.android.core.Twitter;

public class YueApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Twitter.initialize(this);
        Fresco.initialize(this);
    }
}
