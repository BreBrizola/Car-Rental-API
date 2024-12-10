package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record ReservationDTO (
        @JsonProperty("id") Long id,
        @JsonProperty("confirmationNumber") String confirmationNumber,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("mail") String email,
        @JsonProperty("phone") String phone,
        @JsonProperty("totalPrice") double totalPrice,
        @JsonProperty("pickupDate") LocalDate pickupDate,
        @JsonProperty("returnDate")LocalDate returnDate,
        @JsonProperty("pickupTime") String pickupTime,
        @JsonProperty("returnTime") String returnTime,

        @JsonProperty("pickupLocation") LocationDTO pickupLocation,

        @JsonProperty("returnLocation") LocationDTO returnLocation,

        @JsonProperty("vehicle") VehicleDTO vehicle,

        @JsonProperty("additionalProducts") List<AdditionalProductDTO> additionalProducts
) {
}
