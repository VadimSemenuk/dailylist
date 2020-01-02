package com.mamindeveloper.dailylist.Services;

import com.mamindeveloper.dailylist.Api.AuthApi;
import com.mamindeveloper.dailylist.Repositories.AuthRepository;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "http://192.168.100.3:3001/api/";
//    private static final String BASE_URL = "http://ec2-54-93-83-78.eu-central-1.compute.amazonaws.com:3002/api/";
    private Retrofit mRetrofit;

    private NetworkService() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        Interceptor headerAuthorizationInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request();
                if (AuthRepository.getInstance().hasToken()) {
                    Headers headers = request.headers().newBuilder().add("Authorization", AuthRepository.getInstance().getToken()).build();
                    request = request.newBuilder().headers(headers).build();
                }
                return chain.proceed(request);
            }
        };
        clientBuilder.addInterceptor(headerAuthorizationInterceptor);
        OkHttpClient httpClient = clientBuilder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public AuthApi getAuthApi() {
        return mRetrofit.create(AuthApi.class);
    }
}
