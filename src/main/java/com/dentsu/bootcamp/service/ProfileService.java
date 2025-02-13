package com.dentsu.bootcamp.service;
import com.dentsu.bootcamp.dto.ProfileDTO;
import com.dentsu.bootcamp.model.AddressEntity;
import com.dentsu.bootcamp.model.DriversLicenseEntity;
import com.dentsu.bootcamp.model.ProfileEntity;
import com.dentsu.bootcamp.repository.DriversLicenseRepository;
import com.dentsu.bootcamp.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
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

    public Maybe<ProfileDTO> userProfileSearch(String driversLicenseNumber, String lastName, String issuingCountry, String issuingAuthority) {
        return Maybe.fromCallable(() -> {
            Optional<DriversLicenseEntity> driversLicense = driversLicenseRepository.findByLicenseNumberAndCountryCodeAndCountrySubdivision(
                    driversLicenseNumber, issuingCountry, issuingAuthority
            );

            Optional<ProfileEntity> existingProfile = driversLicense.flatMap(dl -> profileRepository.findByDriversLicenseAndLastName(dl, lastName));

            return existingProfile.map(profile -> {
                ProfileDTO profileDTO = objectMapper.convertValue(profile, ProfileDTO.class);
                profileDTO.setFound(true);
                return profileDTO;
            }).orElse(new ProfileDTO());
        });
    }

    public ProfileDTO getProfile(String loyaltyNumber) {
        Optional<ProfileEntity> profile = profileRepository.findByLoyaltyNumber(loyaltyNumber);

        return objectMapper.convertValue(profile, ProfileDTO.class);
    }

    public Single<ProfileDTO> submitPersonalInformation(ProfileDTO profile) {
        return Single.fromCallable(() -> {
            if (!isLegalAge(profile.getDateOfBirth(), profile.getAddress().getCountry())) {
                throw new IllegalArgumentException("User must be at Legal Age");
            }
            if (!isValidEmail(profile.getEmail())) {
                throw new IllegalArgumentException("Provided email is invalid, please try again");
            }
            if (!isDriversLicenseValid(profile.getDriversLicense().getLicenseExpirationDate())) {
                throw new IllegalArgumentException("Driver's License is expired.");
            }

            profile.setLoyaltyNumber(generateLoyaltyNumber());
            profileRepository.save(objectMapper.convertValue(profile, ProfileEntity.class));

            return profile;
        });
    }

    public Single<ProfileDTO> editPersonalInformation(String loyaltyNumber, ProfileDTO updatedProfile) {
        return Single.fromCallable(() ->
                profileRepository.findByLoyaltyNumber(loyaltyNumber)
                        .map(existingProfile -> {
                            existingProfile.setPhone(updatedProfile.getPhone());

                            AddressEntity addressEntity = objectMapper.convertValue(updatedProfile.getAddress(), AddressEntity.class);
                            existingProfile.setAddress(addressEntity);

                            LocalDate updatedExpirationDate = updatedProfile.getDriversLicense().getLicenseExpirationDate();

                            if (updatedExpirationDate.isBefore(LocalDate.now())) {
                                throw new IllegalArgumentException("Driver license expiration date cannot be in the past");
                            }

                            existingProfile.getDriversLicense().setLicenseExpirationDate(updatedExpirationDate);

                            profileRepository.save(existingProfile);
                            return objectMapper.convertValue(existingProfile, ProfileDTO.class);
                        })
                        .orElseThrow(() -> new RuntimeException("Profile not found with loyalty number: " + loyaltyNumber))
        );
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

