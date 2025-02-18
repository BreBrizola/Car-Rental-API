package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VehicleDTO implements Serializable {
    @JsonProperty("id") private long id;
    @JsonProperty("model")private String model;
    @JsonProperty("price")private double price;
    @JsonProperty("terms")private List<VehicleTermsDTO> vehicleTerms;
}
