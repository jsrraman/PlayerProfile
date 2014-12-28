package com.rajaraman.playerprofile;

import android.app.Application;
import android.content.Context;

import java.util.List;

public class PlayerProfileApp extends Application {
    private static PlayerProfileApp mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
    }

    public static PlayerProfileApp getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }
}