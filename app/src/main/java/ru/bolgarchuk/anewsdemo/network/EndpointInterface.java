package ru.bolgarchuk.anewsdemo.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.bolgarchuk.anewsdemo.model.TopNews;

public interface EndpointInterface {
    @GET("regions/{country}/top")
    public Call<List<TopNews>> topRegionNews(@Path("country") String country);

    @GET("regions/{country}/top/max_id/{max_id}")
    public Call<List<TopNews>> topRegionNewsPage(@Path("country") String country, @Path("max_id") int id);
}