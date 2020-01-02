package com.mamindeveloper.dailylist;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

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
