package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.service.VehicleService;
import io.reactivex.rxjava3.core.Observable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Retrieve all the vehicles")
    @GetMapping("/ListAll")
    public Observable<List<VehicleDTO>> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @Operation(summary = "Retrieve a specific vehicle, searched by Id", description = "Pass the vehicle id (number).")
    @GetMapping("/{id}")
    public Observable<VehicleDTO> getVehicleById(@Parameter(description = "The unique identifier of the vehicle.")@PathVariable(value = "id") Long id) {
        return vehicleService.getVehicleById(id);
    }
}
