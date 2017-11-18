package com.levirgon.tutexplocation.Retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by noushad on 9/20/17.
 */

public class ServiceGenerator {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/";

    private static Retrofit.Builder retroBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);


    private static Retrofit retrofit;

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static synchronized <S> S createService(Class<S> serviceClass) {
        if (retrofit == null) {
            if (!httpClient.interceptors().contains(logging)) {
                httpClient.addInterceptor(logging);
                retroBuilder.client(httpClient.build());
                retrofit = retroBuilder.build();
            }
        }
        return retrofit.create(serviceClass);
    }
}