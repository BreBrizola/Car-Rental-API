package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository  extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByDriversLicense_LicenseNumberAndLastNameAndDriversLicense_CountryCodeAndDriversLicense_CountrySubdivision(
            String licenseNumber,
            String lastName,
            String countryCode,
            String countrySubdivision
    );
}
