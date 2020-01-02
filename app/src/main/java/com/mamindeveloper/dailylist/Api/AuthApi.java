package com.mamindeveloper.dailylist.Api;

import com.mamindeveloper.dailylist.Login.LoginBody;
import com.mamindeveloper.dailylist.Login.LoginResponse;
import com.mamindeveloper.dailylist.Register.RegisterBody;
import com.mamindeveloper.dailylist.Register.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("auth/sign-in")
    public Call<LoginResponse> signIn(@Body LoginBody body);

    @POST("auth/sign-up")
    public Call<RegisterResponse> signUp(@Body RegisterBody body);
}
