package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressDTO implements Serializable {
    @JsonProperty("id") private Long id;
    @JsonProperty("city") private String city;
    @JsonProperty("country") private String country;
    @JsonProperty("countrySubdivisionCode") private String countrySubdivisionCode;
    @JsonProperty("postal") private String postal;
    @JsonProperty("streetAddresses") private String streetAddresses;
}
