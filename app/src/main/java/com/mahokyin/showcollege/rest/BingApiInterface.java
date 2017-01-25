package com.mahokyin.showcollege.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by mahokyin on 1/22/17.
 */

public interface BingApiInterface {
    @GET("search?")
    Call<ImageJsonList> getFirstImage(@Query("q") String searchQuery, @Header("Ocp-Apim-Subscription-Key") String apiKey);
}
