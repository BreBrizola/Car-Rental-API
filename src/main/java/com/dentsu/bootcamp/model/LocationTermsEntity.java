package com.dentsu.bootcamp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Entity (name = "location_terms")
public class LocationTermsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonBackReference
    @ToString.Exclude
    private LocationEntity location;

    @ManyToOne
    @JoinColumn(name = "terms_id")
    @ToString.Exclude
    private TermsEntity terms;
}
