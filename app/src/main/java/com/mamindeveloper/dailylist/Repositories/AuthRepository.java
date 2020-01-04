package com.mamindeveloper.dailylist.Repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mamindeveloper.dailylist.App;
import com.mamindeveloper.dailylist.Models.User;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class AuthRepository {
    private static final AuthRepository ourInstance = new AuthRepository();

    public static AuthRepository getInstance() {
        return ourInstance;
    }

    private AuthRepository() {
    }

    public void setUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);

        SharedPreferences prefs = App.getAppContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("user", json);
        prefEdit.commit();
    }

    public User getUser() {
        ArrayList<User.Backup> backups = new ArrayList<>();
        backups.add(new User.Backup(0, DateTime.now(), false));
        backups.add(new User.Backup(0, DateTime.now(), false));
        backups.add(new User.Backup(0, DateTime.now(), true));
        backups.add(new User.Backup(0, DateTime.now(), true));
        return new User(0, "vadim54787@gmail.com", "Vadim Semenyuk", backups);
    }
    public User _getUser() {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        SharedPreferences prefs = App.getAppContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String json = prefs.getString("user", "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public void setToken(String token) {
        SharedPreferences prefs = App.getAppContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("token", token);
        prefEdit.commit();
    }

    public String getToken() {
        SharedPreferences prefs = App.getAppContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        return token;
    }

    public Boolean hasToken() {
        return true;
    }
    public Boolean _hasToken() {
        return !TextUtils.isEmpty(getToken());
    }

    public void signOut() {
        SharedPreferences prefs = App.getAppContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.remove("token");
        prefEdit.remove("user");
        prefEdit.commit();
    }
}
