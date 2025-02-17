package com.dentsu.bootcamp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Entity (name = "terms")
public class TermsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String code;
    private boolean active;

    @OneToMany(mappedBy = "terms")
    private List<VehicleTermsEntity> vehicleTerms;

    @OneToMany(mappedBy = "terms")
    private List<LocationTermsEntity> locationTerms;
}
