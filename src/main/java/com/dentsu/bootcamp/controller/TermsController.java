package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.TermsDTO;
import com.dentsu.bootcamp.dto.VehicleDTO;

import com.dentsu.bootcamp.service.TermsService;
import io.reactivex.rxjava3.core.Single;
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

    @GetMapping("/vehicle")
    public Single<List<TermsDTO>> getVehicleTerms(@RequestParam Long id){
        return termsService.getVehicleTerms(id);
    }

    @GetMapping("/location")
    public Single<List<TermsDTO>> getLocationTerms(@RequestParam Long id){
        return termsService.getLocationTerms(id);
    }

    @PostMapping("/{vehicleId}/add_vehicle_terms")
    public Single<VehicleDTO> addTermsToVehicle(@PathVariable Long vehicleId, @RequestBody List<Long> termsId){
        return termsService.addTermsToVehicle(vehicleId,termsId);
    }

    @PostMapping("/{locationId}/add_location_terms")
    public Single<LocationDTO> addTermsToLocation(@PathVariable Long locationId, @RequestBody List<Long> termsId){
        return termsService.addTermsToLocation(locationId,termsId);
    }
}
