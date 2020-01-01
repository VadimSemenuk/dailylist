package com.mamindeveloper.dailylist;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class App extends Application {
    private static Context mContext;


    SharedPreferences sharedPreferences;

    public static Context getAppContext() {
        return App.mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean("darkTheme", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        mContext = getApplicationContext();
    }
}