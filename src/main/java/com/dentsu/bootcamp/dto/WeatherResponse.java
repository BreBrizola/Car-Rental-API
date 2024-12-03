package com.dentsu.bootcamp.dto;

import lombok.Data;

@Data
public class WeatherResponse {
   private WeatherLocation location;
   private WeatherCurrent current;
}
