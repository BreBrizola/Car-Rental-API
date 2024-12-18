package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.exception.VehicleNotFoundException;
import com.dentsu.bootcamp.repository.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    private final ObjectMapper objectMapper;

    public VehicleService(VehicleRepository vehicleRepository, ObjectMapper objectMapper){
        this.vehicleRepository = vehicleRepository;
        this.objectMapper = objectMapper;
    }

    public Observable<List<VehicleDTO>> getAllVehicles() {
        return Observable.fromCallable(() -> vehicleRepository.findAll())
                .flatMap(list -> Observable.fromIterable(list)
                .map(vehicle -> objectMapper.convertValue(vehicle, VehicleDTO.class))
                .toList()
                .toObservable());
    }

    public Observable<VehicleDTO> getVehicleById(Long id) {
        return Observable.fromCallable(() -> vehicleRepository.findById(id))
                .flatMap(optionalVehicle -> optionalVehicle
                .map(vehicle -> Observable.just(objectMapper.convertValue(vehicle, VehicleDTO.class)))
                .orElse(Observable.error(new VehicleNotFoundException("Vehicle not found"))));
    }
}
