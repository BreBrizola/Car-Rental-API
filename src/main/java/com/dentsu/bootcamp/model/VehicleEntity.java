package com.dentsu.bootcamp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@Entity (name = "vehicle")
public class VehicleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String model;
    private double price;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonIgnore
    private LocationEntity location;

    @OneToMany(mappedBy = "vehicle")
    private List<AdditionalProductEntity> additionalProducts;

    @OneToMany(mappedBy = "vehicle")
    @JsonManagedReference
    @ToString.Exclude
    private List<VehicleTermsEntity> vehicleTerms;
}
