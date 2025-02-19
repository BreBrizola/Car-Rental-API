package com.dentsu.bootcamp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Entity (name = "vehicle_terms")
public class VehicleTermsEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference
    @ToString.Exclude
    private VehicleEntity vehicle;

    @ManyToOne
    @JoinColumn(name = "terms_id")
    @ToString.Exclude
    private TermsEntity terms;
}
