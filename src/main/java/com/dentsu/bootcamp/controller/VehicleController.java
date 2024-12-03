package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @Operation(summary = "Retrieve all the vehicles")
    @GetMapping("/ListAll")
    public List<VehicleEntity> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @Operation(summary = "Retrieve a specific vehicle, searched by Id", description = "Pass the vehicle id (number).")
    @GetMapping("/{id}")
    public Optional<VehicleEntity> getVehicleById(@Parameter(description = "The unique identifier of the vehicle.")@PathVariable(value = "id") Long id) {
        return vehicleService.getVehicleById(id);
    }
}
