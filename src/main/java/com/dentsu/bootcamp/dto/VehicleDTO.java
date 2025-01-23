package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record VehicleDTO(
        @JsonProperty("id")long id,
        @JsonProperty("model")String model,
        @JsonProperty("price")double price
) implements Serializable {
}
