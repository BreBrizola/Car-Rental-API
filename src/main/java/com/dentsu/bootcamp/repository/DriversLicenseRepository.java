package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.DriversLicenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriversLicenseRepository extends JpaRepository<DriversLicenseEntity, Long> {
    public Optional<DriversLicenseEntity> findByLicenseNumberAndCountryCodeAndCountrySubdivision(String licenseNumber, String countryCode, String countrySubdivision);
}
