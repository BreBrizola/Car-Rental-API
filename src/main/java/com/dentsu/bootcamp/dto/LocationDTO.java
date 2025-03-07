package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LocationDTO implements Serializable {
    @JsonProperty("id") private Long id;
    @JsonProperty("name") private String name;
    @JsonProperty("address") private String address;
    @JsonProperty("openingHours") private String openingHours;
    @JsonProperty("afterHoursFeed") private Long afterHoursFee;
    @JsonProperty("terms")private List<LocationTermsDTO> locationTerms;
}
