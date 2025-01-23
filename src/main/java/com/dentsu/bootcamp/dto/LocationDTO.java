package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record LocationDTO (
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("address") String address,
        @JsonProperty("openingHours") String openingHours,
        @JsonProperty("afterHoursFeed") Long afterHoursFee,
        @JsonProperty("weather") WeatherResponse weather
) implements Serializable {
}
