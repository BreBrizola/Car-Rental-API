package com.dentsu.bootcamp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;

@Data
@Entity(name = "address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String country;

    @Column(name = "COUNTRY_SUBDIVISION")
    private String countrySubdivisionCode;
    private String postal;
    private String streetAddresses;
}
