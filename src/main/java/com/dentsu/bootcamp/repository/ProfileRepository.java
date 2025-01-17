package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.DriversLicenseEntity;
import com.dentsu.bootcamp.model.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository  extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByDriversLicenseAndLastName(
            DriversLicenseEntity driversLicense, String lastName
    );

    Optional<ProfileEntity> findByLoyaltyNumber(String loyaltyNumber);
}
