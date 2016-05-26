package ru.bolgarchuk.anewsdemo.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public static final String BASE_URL = "http://anews.com/api/v2/";

    public static EndpointInterface getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndpointInterface apiService = retrofit.create(EndpointInterface.class);
        return apiService;
    }
}
