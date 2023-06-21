package com.example.RetrofitApiCaller;
import com.example.RetrofitApiCaller.apicall;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFetch {
    private  static RetroFetch instance;
    public apicall Api;
    static Gson gson = new GsonBuilder().setLenient().create();
    private RetroFetch(String Url){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Url).addConverterFactory(GsonConverterFactory.create()).build();
       Api= retrofit.create(apicall.class);  
    }
    
    public static RetroFetch getInstance(String Url){
        if(instance==null){
            instance= new RetroFetch(Url);
        }
        return instance;
    }
}
