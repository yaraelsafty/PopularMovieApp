package com.example.yara.popularmovieapp.Utils.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yara on 01-Nov-18.
 */

public class ApiClient {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String api_key="";
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
