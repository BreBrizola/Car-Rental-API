package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.model.ProfileEntity;
import com.dentsu.bootcamp.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("enroll")
public class ProfileController {
    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profileSearch")
    public ResponseEntity<String> searchUserProfile(
            @RequestParam String driversLicenseNumber,
            @RequestParam String lastName,
            @RequestParam String issuingCountry,
            @RequestParam(required = false) String issuingAuthority) {

        return profileService.userProfileSearch(driversLicenseNumber, lastName, issuingCountry, issuingAuthority);
    }

    @PostMapping("/createProfile")
    public ResponseEntity<String> submitPersonalInformation(@RequestBody @Valid ProfileEntity profileEntity){
        return profileService.submitPersonalInformation(profileEntity);
    }
}
