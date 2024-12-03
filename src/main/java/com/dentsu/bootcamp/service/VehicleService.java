package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<VehicleEntity> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<VehicleEntity> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }
}
