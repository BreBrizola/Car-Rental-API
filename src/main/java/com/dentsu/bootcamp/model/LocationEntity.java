package com.dentsu.bootcamp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@Entity (name = "location")
public class LocationEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String openingHours;
    private Long afterHoursFeed;

    @OneToMany(mappedBy = "location")
    private List<VehicleEntity> vehicleList;

    @OneToMany(mappedBy = "location")
    @JsonManagedReference
    @ToString.Exclude
    private List<LocationTermsEntity> locationTerms;
}
