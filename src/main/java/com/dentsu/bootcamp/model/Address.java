package com.dentsu.bootcamp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
    private String city;
    private String country;

    @Column(name = "COUNTRY_SUBDIVISION")
    private String countrySubdivisionCode;
    private String postal;
    private String streetAddresses;
}
