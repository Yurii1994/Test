package com.muv.test;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LinkApi
{
    @GET("/test/test.php")
    Call<List<GetModel>> getData(@Query("action") String action);
}
