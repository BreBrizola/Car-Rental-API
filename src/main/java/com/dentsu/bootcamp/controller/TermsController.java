package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.TermsDTO;
import com.dentsu.bootcamp.dto.VehicleDTO;

import com.dentsu.bootcamp.service.TermsService;
import io.reactivex.rxjava3.core.Single;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("terms")
public class TermsController {
    private TermsService termsService;

    public TermsController(TermsService termsService) {
        this.termsService = termsService;
    }

    @Operation(summary = "Retrieve terms for a specific vehicle",
            description = "Pass the vehicle ID to retrieve the associated terms.")
    @GetMapping("/vehicle")
    public Single<List<TermsDTO>> getVehicleTerms(@RequestParam Long id){
        return termsService.getVehicleTerms(id);
    }

    @Operation(summary = "Retrieve terms for a specific location",
            description = "Pass the location ID to retrieve the associated terms.")
    @GetMapping("/location")
    public Single<List<TermsDTO>> getLocationTerms(@RequestParam Long id){
        return termsService.getLocationTerms(id);
    }

    @Operation(summary = "Add terms to a vehicle",
            description = "Pass the vehicle ID and a list of term IDs to associate them with the vehicle.")
    @PostMapping("/{vehicleId}/add_vehicle_terms")
    public Single<VehicleDTO> addTermsToVehicle(@PathVariable Long vehicleId, @RequestBody List<Long> termsId){
        return termsService.addTermsToVehicle(vehicleId,termsId);
    }

    @Operation(summary = "Add terms to a location",
            description = "Pass the location ID and a list of term IDs to associate them with the location.")
    @PostMapping("/{locationId}/add_location_terms")
    public Single<LocationDTO> addTermsToLocation(@PathVariable Long locationId, @RequestBody List<Long> termsId){
        return termsService.addTermsToLocation(locationId,termsId);
    }
}
