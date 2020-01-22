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
        db.execSQL("create table notes ("
                + "id integer primary key autoincrement,"
                + "colorId integer,"
                + "date integer,"
                + "startDateTime integer,"
                + "endDateTime integer,"
                + "isFinished integer,"
                + "isNotificationEnabled integer,"
                + "lastActionDate integer,"
                + "lastAction integer,"
                + "type integer,"
                + "title text,"
                + "contentFields text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
