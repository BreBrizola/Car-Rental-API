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
@Entity(name = "driversLicense")
public class DriversLicenseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String countryCode;

    @Column(name = "DRIVERS_LICENSE_STATE")
    private String countrySubdivision;

    @Column(name = "DRIVERS_LICENSE_EXPIRY")
    private LocalDate licenseExpirationDate;

    @Column(name = "DRIVERS_LICENSE_NUMBER")
    private String licenseNumber;
}
