package com.dentsu.bootcamp.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
    private AddressType addressType;
    private String city;
    private String country;
    private String countrySubdivisionCode;
    private String postal;
    private String streetAddresses;

    public enum AddressType {
        HOME,
        WORK
    }
}
