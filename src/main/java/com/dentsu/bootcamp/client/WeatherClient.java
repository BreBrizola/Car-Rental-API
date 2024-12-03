package com.dentsu.bootcamp.client;

import com.dentsu.bootcamp.dto.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weatherClient", url="http://api.weatherapi.com/v1/")
public interface WeatherClient {

    @GetMapping("/current.json")
    WeatherResponse getCurrentWeather(@RequestParam("key") String apiKey,
                                      @RequestParam ("q") String city,
                                      @RequestParam ("aqi") String airQuality);
}
