package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.DriversLicenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriversLicenseRepository extends JpaRepository<DriversLicenseEntity, Long> {
    @Query("SELECT d FROM drivers_license d WHERE d.licenseNumber = :licenseNumber " +
            "AND d.countryCode = :countryCode AND d.countrySubdivision = :countrySubdivision")
    Optional<DriversLicenseEntity> findByLicenseNumberAndCountryCodeAndCountrySubdivision(
            @Param("licenseNumber") String licenseNumber,
            @Param("countryCode") String countryCode,
            @Param("countrySubdivision") String countrySubdivision
    );

}
