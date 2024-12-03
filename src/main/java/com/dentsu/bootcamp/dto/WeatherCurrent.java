package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherCurrent {
    private WeatherCondition condition;

    @JsonProperty("temp_c")
    private String tempC;

}
