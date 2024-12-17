package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VehicleDTO(
        @JsonProperty("id")long id,
        @JsonProperty("model")String model,
        @JsonProperty("price")double price
){
}
