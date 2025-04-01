package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.dto.ProfileDTO;
import com.dentsu.bootcamp.service.ProfileService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("enroll")
public class ProfileController {
    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "Search for a user profile",
            description = "Pass the driver's license number, last name, and issuing country. Optionally, you can pass the issuing authority.")
    @GetMapping("/profileSearch")
    public Maybe<ProfileDTO> searchUserProfile(
            @RequestParam String driversLicenseNumber,
            @RequestParam String lastName,
            @RequestParam String issuingCountry,
            @RequestParam(required = false) String issuingAuthority) {

        return profileService.userProfileSearch(driversLicenseNumber, lastName, issuingCountry, issuingAuthority);
    }

    @Operation(summary = "Create a new user profile",
            description = "Pass personal information to create a profile.(First name, last name, phone, email, date of birth, address, driver's license, and login information)")
    @PostMapping("/createProfile")
    public Single<ProfileDTO> submitPersonalInformation(@RequestBody @Valid ProfileDTO profile) {
        return profileService.submitPersonalInformation(profile);
    }

    @Operation(summary = "Retrieve user profile",
            description = "Pass the loyalty number to retrieve the profile details.")
    @GetMapping("/{loyaltyNumber}")
    public ProfileDTO getProfile(@PathVariable String loyaltyNumber) {
        return profileService.getProfile(loyaltyNumber);
    }

    @Operation(summary = "Edit user profile",
            description = "Pass the loyalty number and updated profile information.")
    @PutMapping("/editProfile")
    public Single<ProfileDTO> editProfile(@RequestParam String loyaltyNumber, @RequestBody ProfileDTO updatedProfile) {
        return profileService.editPersonalInformation(loyaltyNumber, updatedProfile);
    }

    @Operation(summary = "Get states and provinces by country",
            description = "Pass the country name to retrieve its states or provinces. The only countries that has states or provinces are USA and CA")
    @GetMapping("/StatesAndProvinces/{country}")
    public List<String> getStatesAndProvinces(@PathVariable String country) {
        return profileService.getStatesAndProvinces(country);
    }
}
