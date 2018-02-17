package com.robillo.test_project.network.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by robinkamboj on 11/11/17.
 */

public class ApiClient {

    private static final String BASE_URL = "http://www.androidbegin.com/tutorial/";
    private static Retrofit mRetrofitInstance;

    public static Retrofit getClient(){
        if(mRetrofitInstance == null){
            mRetrofitInstance = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return mRetrofitInstance;
    }
}
