package com.dentsu.bootcamp.client;

import com.dentsu.bootcamp.dto.WeatherResponse;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRetroFitClient {

    @GET("/current.json")
    Observable<WeatherResponse> getCurrentWeather(@Query("key") String apiKey,
                                                  @Query ("q") String city,
                                                  @Query ("aqi") String airQuality);
}
