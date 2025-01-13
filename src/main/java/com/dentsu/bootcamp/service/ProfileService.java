package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.model.ProfileEntity;
import com.dentsu.bootcamp.repository.ProfileRepository;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertFalseValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProfileService {
    private final AssertFalseValidator assertFalseValidator;
    private ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository, AssertFalseValidator assertFalseValidator){
        this.profileRepository = profileRepository;
        this.assertFalseValidator = assertFalseValidator;
    }

    public ResponseEntity<String> userProfileSearch(String driversLicenseNumber, String lastName, String issuingCountry, String issuingAuthority){
        Optional<ProfileEntity> existingProfile = profileRepository.findByDriversLicense_LicenseNumberAndLastNameAndDriversLicense_CountryCodeAndDriversLicense_CountrySubdivision(
                driversLicenseNumber,
                lastName,
                issuingCountry,
                issuingAuthority
        );
        if (existingProfile.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already an account with this driver's license, please login");
        } else {
            return ResponseEntity.ok("Account alright to continue the enroll process");
        }
    }

    public ProfileEntity submitPersonalInformation(ProfileEntity profileEntity){
        if (!isLegalAge(profileEntity.getDateOfBirth(), profileEntity.getAddress().getCountry())) {
        throw new IllegalArgumentException("User must be at Legal Age");
        }
        if(!isValidEmail(profileEntity.getEmail())){
        throw new IllegalArgumentException("Provided email is invalid, please try again");
        }

        if(!isDriversLicenseValid(profileEntity.getDriversLicense().getLicenseExpirationDate())){
            throw new IllegalArgumentException("Driver's License is expired.");
        }
    }

    private boolean isLegalAge(LocalDate dateOfBirth, String country){
        int legalAge = country.equalsIgnoreCase("USA") ? 21 : 18;
        return LocalDate.now().minusYears(legalAge).isAfter(dateOfBirth);
    }
    //Nos estados unidos o usuario precisa ser maior de 21

    private boolean isValidEmail(String email){
        int index = email.indexOf("@");
        if (index <= 0 || index == email.length() - 1) {
            return false;
        }

        int dotIndex = email.indexOf(".");
        if(dotIndex <= index + 1){
            return false;
        }

        return true;
    }

    private boolean isDriversLicenseValid(LocalDate expirationDate){
        return expirationDate.isAfter(LocalDate.now());
    }
}