package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record ReservationDTO (
        @JsonProperty("id") Long id,
        @JsonProperty("confirmationNumber") @NotBlank(message = "Loyalty number is required") String confirmationNumber,
        @JsonProperty("firstName") @NotBlank(message = "First name is required") String firstName,
        @JsonProperty("lastName") @NotBlank(message = "Last name is required") String lastName,
        @JsonProperty("email") @NotBlank(message = "Email is required") String email,
        @JsonProperty("phone") @NotBlank(message = "Phone number is required")String phone,
        @JsonProperty("totalPrice") double totalPrice,
        @JsonProperty("pickupDate") LocalDate pickupDate,
        @JsonProperty("returnDate")LocalDate returnDate,
        @JsonProperty("pickupTime") String pickupTime,
        @JsonProperty("returnTime") String returnTime,

        @JsonProperty("pickupLocation") LocationDTO pickupLocation,

        @JsonProperty("returnLocation") LocationDTO returnLocation,

        @JsonProperty("vehicle") VehicleDTO vehicle,

        @JsonProperty("additionalProducts") List<AdditionalProductDTO> additionalProducts
) implements Serializable {
}
