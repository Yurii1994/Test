package com.muv.test;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application
{
    private String url_data = "http://technicalplan.ho.ua";
    private static LinkApi linkApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(url_data)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        linkApi = retrofit.create(LinkApi.class);

    }

    public static LinkApi getApi()
    {
        return linkApi;
    }
}
