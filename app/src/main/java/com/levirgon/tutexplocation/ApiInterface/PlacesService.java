package com.levirgon.tutexplocation.ApiInterface;

import com.levirgon.tutexplocation.model.Places;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by noushad on 11/15/17.
 */

public interface PlacesService {

    @GET("json?")
    Call<Places> getPlacesByOrder(
            @Query("key") String api_key,
            @Query("location") String location,
            @Query("rankby") String sortBy,
            @Query("type") String placeType
    );

    @GET("json?")
    Call<Places> getPlacesByRadius(
            @Query("key") String api_key,
            @Query("location") String location,
            @Query("radius") double radius,
            @Query("type") String placeType
    );
}
