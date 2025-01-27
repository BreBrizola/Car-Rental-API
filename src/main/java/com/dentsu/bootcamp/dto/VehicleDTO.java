package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleDTO implements Serializable {
    @JsonProperty("id") private long id;
    @JsonProperty("model")private String model;
    @JsonProperty("price")private double price;
}
