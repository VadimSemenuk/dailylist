package com.mamindeveloper.dailylist.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mamindeveloper.dailylist.Models.User;

public class LoginResponse {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private User user;

    public LoginResponse(String token, int errorCode, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
