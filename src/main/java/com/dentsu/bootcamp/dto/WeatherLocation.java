package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherLocation {
    private String name;
    private String region;
    private String country;

    @JsonProperty("tz_id")
    private String tzId;
}
