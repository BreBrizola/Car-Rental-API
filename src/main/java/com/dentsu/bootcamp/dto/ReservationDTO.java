package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonSerialize(as = ReservationDTO.class)
public class ReservationDTO implements Serializable {
    @JsonProperty("id") private Long id;

    @JsonProperty("confirmationNumber") @NotBlank(message = "Loyalty number is required")
    private String confirmationNumber;

    @JsonProperty("firstName") @NotBlank(message = "First name is required")
    private String firstName;

    @JsonProperty("lastName") @NotBlank(message = "Last name is required")
    private String lastName;

    @JsonProperty("email") @NotBlank(message = "Email is required")
    private String email;

    @JsonProperty("phone") @NotBlank(message = "Phone number is required")
    private String phone;

    @JsonProperty("totalPrice")
    private double totalPrice;

    @JsonProperty("pickupDate")
    private LocalDate pickupDate;

    @JsonProperty("returnDate")
    private LocalDate returnDate;

    @JsonProperty("pickupTime")
    private String pickupTime;

    @JsonProperty("returnTime")
    private String returnTime;

    @JsonProperty("profile")
    private ProfileDTO profile;

    @JsonProperty("pickupLocation")
    LocationDTO pickupLocation;

    @JsonProperty("returnLocation")
    LocationDTO returnLocation;

    @JsonProperty("vehicle")
    VehicleDTO vehicle;

    @JsonProperty("additionalProducts")
    List<AdditionalProductDTO> additionalProducts;

}
