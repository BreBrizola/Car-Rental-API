package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonSerialize(as = ReservationDTO.class)
public class ReservationDTO implements Serializable {
    @JsonProperty("id") private Long id;

    @JsonProperty("confirmationNumber")
    private String confirmationNumber;

    @JsonProperty("firstName")
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 30)
    private String firstName;

    @JsonProperty("lastName")
    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 80)
    private String lastName;

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    @Size(min = 1, max = 100)
    private String email;

    @JsonProperty("phone")
    @NotBlank(message = "Phone number is required")
    @Size(min = 20, max = 20)
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
