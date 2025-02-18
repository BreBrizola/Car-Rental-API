package com.dentsu.bootcamp.dto;

import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.TermsEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LocationTermsDTO implements Serializable {
    @JsonProperty("id")private Long id;

    @JsonProperty("location")private LocationEntity location;

    @JsonProperty("terms")private TermsEntity terms;
}
