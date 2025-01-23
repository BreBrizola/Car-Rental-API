package com.dentsu.bootcamp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity(name = "profile")
public class ProfileEntity implements Serializable {
    @Id
    private String loyaltyNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driversLicense_id")
    private DriversLicenseEntity driversLicense;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "login_id")
    private LoginEntity login;
}
