package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ProfileDTO implements Serializable {
        @JsonProperty("loyaltyNumber")
        private String loyaltyNumber;

        @JsonProperty("firstName")
        @NotEmpty(message = "First name is required.")
        private String firstName;

        @JsonProperty("lastName")
        @NotEmpty(message = "Last name is required.")
        private String lastName;

        @JsonProperty("email")
        @NotEmpty(message = "Email is required.")
        private String email;

        @JsonProperty("phone")
        @NotEmpty(message = "Phone is required.")
        private String phone;

        @JsonProperty("dateOfBirth")
        @NotNull(message = "Date of birth is required.")
        private LocalDate dateOfBirth;

        @JsonProperty("address")
        @NotNull(message = "Address is required.")
        private AddressDTO address;

        @JsonProperty("driversLicense")
        @NotNull(message = "Driver's license is required.")
        private DriversLicenseDTO driversLicense;

        @JsonProperty("login")
        @NotNull(message = "Login credentials are required.")
        private LoginDTO login;

        @JsonProperty("found")
        private boolean found = false;
}
