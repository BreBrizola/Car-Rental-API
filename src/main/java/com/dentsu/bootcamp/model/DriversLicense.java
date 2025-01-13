package com.dentsu.bootcamp.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;

@Data
@Embeddable
public class DriversLicense {
    private String countryCode;
    private String countrySubdivision;
    private LocalDate licenseExpirationDate;
    private String licenseNumber;
}
