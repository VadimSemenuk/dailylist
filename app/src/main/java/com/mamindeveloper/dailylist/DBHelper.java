package com.mamindeveloper.dailylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance = null;

    public DBHelper(Context context) {
        super(context, "DailyListDB", null, 1);
    }

    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper(App.getAppContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("app-log", "--- onCreate database ---");
        db.execSQL("create table notes ("
                + "id integer primary key autoincrement,"
                + "title text,"
                + "description text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("app-log", "--- onUpgrade database ---");
    }
}
