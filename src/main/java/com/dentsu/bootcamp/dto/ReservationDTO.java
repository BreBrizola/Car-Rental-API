package com.dentsu.bootcamp.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ReservationDTO {
    private Long id;
    private String confirmationNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private double totalPrice;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private String pickupTime;
    private  String returnTime;

    private LocationDTO pickupLocation;

    private LocationDTO returnLocation;

    private VehicleDTO vehicle;

    private List<AdditionalProductDTO> additionalProducts;
}
