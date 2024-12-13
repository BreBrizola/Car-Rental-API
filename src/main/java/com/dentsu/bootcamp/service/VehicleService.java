package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.dto.AdditionalProductDTO;
import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.repository.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Flowable<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll()
                .map(vehicle -> objectMapper.convertValue(vehicle, VehicleDTO.class));
    }

    public Maybe<VehicleDTO> getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicle -> objectMapper.convertValue(vehicle, VehicleDTO.class));
    }
}
