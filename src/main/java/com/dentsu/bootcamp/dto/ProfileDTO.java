package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ProfileDTO implements Serializable {
        @JsonProperty("loyaltyNumber")
        private String loyaltyNumber;

        @JsonProperty("firstName")
        @NotEmpty(message = "First name is required.")
        @Size(min = 1, max = 30)
        private String firstName;

        @JsonProperty("lastName")
        @NotEmpty(message = "Last name is required.")
        @Size(min = 1, max = 80)
        private String lastName;

        @JsonProperty("email")
        @NotEmpty(message = "Email is required.")
        @Size(min = 1, max = 100)
        private String email;

        @JsonProperty("phone")
        @NotEmpty(message = "Phone is required.")
        @Size(min = 20, max = 20)
        private String phone;

        @JsonProperty("dateOfBirth")
        @NotNull(message = "Date of birth is required.")
        @Size(min = 8, max = 10)
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
