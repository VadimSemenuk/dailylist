package com.mamindeveloper.dailylist.Api;

import com.mamindeveloper.dailylist.Models.User;
import com.mamindeveloper.dailylist.Register.RegisterBody;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @GET("user/backups")
    public Call<ArrayList<User.Backup>> getUserBackups();

    @POST("user/backup")
    public Call<Void> addBackup(@Body int id);

    @GET("user/backup/{id}")
    public Call<Void> getBackup(@Path("id") int backupId);
}
