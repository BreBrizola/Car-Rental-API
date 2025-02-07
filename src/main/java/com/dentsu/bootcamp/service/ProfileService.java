package com.dentsu.bootcamp.service;
import com.dentsu.bootcamp.dto.ProfileDTO;
import com.dentsu.bootcamp.model.DriversLicenseEntity;
import com.dentsu.bootcamp.model.ProfileEntity;
import com.dentsu.bootcamp.repository.DriversLicenseRepository;
import com.dentsu.bootcamp.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;
    private ObjectMapper objectMapper;
    private DriversLicenseRepository driversLicenseRepository;

    public ProfileService(ProfileRepository profileRepository, ObjectMapper objectMapper, DriversLicenseRepository driversLicenseRepository) {
        this.profileRepository = profileRepository;
        this.objectMapper = objectMapper;
        this.driversLicenseRepository = driversLicenseRepository;
    }

    public ResponseEntity<String> userProfileSearch(String driversLicenseNumber, String lastName, String issuingCountry, String issuingAuthority) {
        Optional<DriversLicenseEntity> driversLicense = driversLicenseRepository.findByLicenseNumberAndCountryCodeAndCountrySubdivision(
                driversLicenseNumber, issuingCountry, issuingAuthority
        );

        Optional<ProfileEntity> existingProfile = driversLicense.flatMap(dl -> profileRepository.findByDriversLicenseAndLastName(dl, lastName));

        if (existingProfile.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already an account with this driver's license, please login");
        } else
            return ResponseEntity.ok("Account alright to continue the enroll process");
    }

    public ProfileDTO getProfile(String loyaltyNumber) {
        Optional<ProfileEntity> profile = profileRepository.findByLoyaltyNumber(loyaltyNumber);

        return objectMapper.convertValue(profile, ProfileDTO.class);
    }

    public ResponseEntity<String> submitPersonalInformation(ProfileEntity profileEntity) {
        try {
            if (!isLegalAge(profileEntity.getDateOfBirth(), profileEntity.getAddress().getCountry())) {
                throw new IllegalArgumentException("User must be at Legal Age");
            }
            if (!isValidEmail(profileEntity.getEmail())) {
                throw new IllegalArgumentException("Provided email is invalid, please try again");
            }
            if (!isDriversLicenseValid(profileEntity.getDriversLicense().getLicenseExpirationDate())) {
                throw new IllegalArgumentException("Driver's License is expired.");
            }

            profileEntity.setLoyaltyNumber(generateLoyaltyNumber());
            profileRepository.save(profileEntity);

            String confirmationMessage = "Enrollment successful! Your loyalty number is " + profileEntity.getLoyaltyNumber();

            return ResponseEntity.ok(confirmationMessage);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public ProfileDTO editPersonalInformation(String loyaltyNumber, ProfileEntity updatedProfile) {
        Optional<ProfileEntity> existingProfileOpt = profileRepository.findByLoyaltyNumber(loyaltyNumber);

        ProfileEntity existingProfile = existingProfileOpt.orElseThrow(() -> new RuntimeException("Profile not found with loyalty number: " + loyaltyNumber)
        );

    existingProfile.setEmail(updatedProfile.getEmail());
    existingProfile.setPhone(updatedProfile.getPhone());
    existingProfile.setAddress(updatedProfile.getAddress());
    existingProfile.setDriversLicense(updatedProfile.getDriversLicense());
    existingProfile.setLogin(updatedProfile.getLogin());

    profileRepository.save(existingProfile);

    return objectMapper.convertValue(existingProfile, ProfileDTO.class);
}

    public List<String> getStatesAndProvinces(String country) {
        List<String> statesAndProvinces = new ArrayList<>();
        if(country.equalsIgnoreCase("USA") || country.equalsIgnoreCase("United States")){
            statesAndProvinces.addAll(Arrays.asList(
                "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN",
                "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV",
                "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN",
                "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
            ));
        }

        if(country.equalsIgnoreCase("CA") || country.equalsIgnoreCase("Canada")) {
            statesAndProvinces.addAll(Arrays.asList(
                "AB", "BC", "MB", "NB", "NL", "NS", "ON", "PE", "QC", "SK"
            ));
        }

        return statesAndProvinces;
    }

    private String generateLoyaltyNumber() {
        UUID loyaltyNumber = UUID.randomUUID();
        return loyaltyNumber.toString();
    }

    private boolean isLegalAge(LocalDate dateOfBirth, String country) {
        int legalAge = country.equalsIgnoreCase("USA") ? 21 : 18;
        return LocalDate.now().minusYears(legalAge).isAfter(dateOfBirth);
    }

    private boolean isValidEmail(String email) {
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

    private boolean isDriversLicenseValid(LocalDate expirationDate) {
        return expirationDate.isAfter(LocalDate.now());
    }
}

