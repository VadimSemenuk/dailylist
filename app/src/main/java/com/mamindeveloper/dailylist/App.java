package com.mamindeveloper.dailylist;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Context mContext;

    public static Context getAppContext() {
        return App.mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }
}