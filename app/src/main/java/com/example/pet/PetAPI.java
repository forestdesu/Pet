package com.example.pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PetAPI {
    @POST("pet")

    Call<Pet> createPost(@Body Pet dataModal);
}