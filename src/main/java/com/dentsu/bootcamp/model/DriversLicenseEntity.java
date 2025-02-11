package com.dentsu.bootcamp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity(name = "drivers_license")
public class DriversLicenseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "drivers_license_state")
    private String countrySubdivision;

    @Column(name = "drivers_license_expiry")
    private LocalDate licenseExpirationDate;

    @Column(name = "drivers_license_number")
    private String licenseNumber;
}
