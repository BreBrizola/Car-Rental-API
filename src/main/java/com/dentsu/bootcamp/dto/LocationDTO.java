package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationDTO (
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("address") String address,
        @JsonProperty("openingHours") String openingHours,
        @JsonProperty("afterHoursFee") Long afterHoursFee,
        @JsonProperty("weather") WeatherResponse weather
) {
}
