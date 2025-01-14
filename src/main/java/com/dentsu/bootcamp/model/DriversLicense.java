package com.dentsu.bootcamp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;

@Data
@Embeddable
public class DriversLicense {
    private String countryCode;

    @Column(name = "DRIVERS_LICENSE_STATE")
    private String countrySubdivision;

    @Column(name = "DRIVERS_LICENSE_EXPIRY")
    private LocalDate licenseExpirationDate;

    @Column(name = "DRIVERS_LICENSE_NUMBER")
    private String licenseNumber;
}
