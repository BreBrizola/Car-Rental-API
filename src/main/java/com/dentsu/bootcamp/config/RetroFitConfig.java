package com.dentsu.bootcamp.config;

import com.dentsu.bootcamp.client.WeatherRetroFitClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetroFitConfig {

    private static final String API_URL = "http://api.weatherapi.com/v1/";

    @Bean
    public Retrofit retroFitWeather (){
        return new Retrofit.Builder()
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(API_URL)
                .build();
    }

    @Bean
    public WeatherRetroFitClient weatherRetroFitClient(){
        return retroFitWeather().create(WeatherRetroFitClient.class);
    }
}
