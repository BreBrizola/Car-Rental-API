package com.dentsu.bootcamp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity (name = "reservation")
public class ReservationEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String confirmationNumber;
    private double totalPrice;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private String pickupTime;
    private  String returnTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loyalty_number")
    private ProfileEntity profile;

    @ManyToOne
    @JoinColumn(name = "pickup_location_id")
    private LocationEntity pickupLocation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "return_location_id")
    private LocationEntity returnLocation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;

    @ManyToMany
    @JoinTable(
            name = "reservation_additional_product",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "additional_product_id")
    )
    private List<AdditionalProductEntity> additionalProducts;
    private boolean checkInEmailSent;
}
