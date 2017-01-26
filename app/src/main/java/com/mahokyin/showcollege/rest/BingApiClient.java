package com.mahokyin.showcollege.rest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mahokyin on 1/22/17.
 */

public class BingApiClient {
    public static final String BASE_URL = "https://api.cognitive.microsoft.com/bing/v5.0/images/";
    public static final String API_KEY = "0757c016a7da45b498844f842d9b2858";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
