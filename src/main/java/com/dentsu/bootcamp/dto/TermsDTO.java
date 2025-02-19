package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TermsDTO implements Serializable {
    @JsonProperty("id") private Long id;
    @JsonProperty("description") private String description;
    @JsonProperty("code") private String code;
    @JsonProperty("active") private boolean active;

    @JsonProperty("vehicleTerms") private List<VehicleTermsDTO> vehicleTerms;

    @JsonProperty("locationTerms") private List<LocationTermsDTO> locationTerms;
}
