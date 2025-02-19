package com.dentsu.bootcamp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity (name = "terms")
public class TermsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String code;
    private boolean active;

    @OneToMany(mappedBy = "terms")
    @JsonIgnore
    @ToString.Exclude
    private List<VehicleTermsEntity> vehicleTerms;

    @OneToMany(mappedBy = "terms")
    @JsonIgnore
    @ToString.Exclude
    private List<LocationTermsEntity> locationTerms;
}
