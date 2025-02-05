package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DriversLicenseDTO implements Serializable {
    @JsonProperty("id") private Long id;
    @JsonProperty("countryCode") private String countryCode;
    @JsonProperty("countrySubdivision") private String countrySubdivision;
    @JsonProperty("licenseExpirationDate") private LocalDate licenseExpirationDate;
    @JsonProperty("licenseNumber") private String licenseNumber;
}
