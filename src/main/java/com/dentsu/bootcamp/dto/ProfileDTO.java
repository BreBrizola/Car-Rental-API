package com.dentsu.bootcamp.dto;

import com.dentsu.bootcamp.model.AddressEntity;
import com.dentsu.bootcamp.model.DriversLicenseEntity;
import com.dentsu.bootcamp.model.LoginEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProfileDTO(
        @JsonProperty("loyaltyNumber")
        String loyaltyNumber,

        @JsonProperty("firstName")
        @NotEmpty(message = "First name is required.")
        String firstName,

        @JsonProperty("lastName")
        @NotEmpty(message = "Last name is required.")
        String lastName,

        @JsonProperty("email")
        @NotEmpty(message = "Email is required.")
        String email,

        @JsonProperty("phone")
        @NotEmpty(message = "Phone is required.")
        String phone,

        @JsonProperty("dateOfBirth")
        @NotNull(message = "Date of birth is required.")
        LocalDate dateOfBirth,

        @JsonProperty("address")
        @NotNull(message = "Address is required.")
        AddressEntity addressEntity,

        @JsonProperty("driversLicense")
        @NotNull(message = "Driver's license is required.")
        DriversLicenseEntity driversLicenseEntity,

        @JsonProperty("login")
        @NotNull(message = "Login credentials are required.")
        LoginEntity login
) {
}
