package com.example.RetrofitApiCaller;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface apicall {
    @GET("/gimme/{q}")
    Call<MemesFetch> getMemes(@Path("q") int q);
}
