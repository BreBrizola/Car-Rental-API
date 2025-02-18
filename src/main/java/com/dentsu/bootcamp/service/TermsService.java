package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.dto.TermsDTO;
import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.model.TermsEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.model.VehicleTermsEntity;
import com.dentsu.bootcamp.repository.TermsRepository;
import com.dentsu.bootcamp.repository.VehicleRepository;
import com.dentsu.bootcamp.repository.VehicleTermsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TermsService {
    private VehicleTermsRepository vehicleTermsRepository;
    private ObjectMapper objectMapper;
    private VehicleRepository vehicleRepository;
    private TermsRepository termsRepository;

    public TermsService (VehicleTermsRepository vehicleTermsRepository, ObjectMapper objectMapper){
        this.vehicleTermsRepository = vehicleTermsRepository;
        this.objectMapper = objectMapper;
    }

    public Single<List<TermsDTO>> getVehicleTerms(VehicleDTO vehicle){
        return Single.fromCallable(() ->
                vehicleTermsRepository.findByVehicle(objectMapper.convertValue(vehicle, VehicleEntity.class)).stream()
                        .map(vehicleTerms -> objectMapper.convertValue(vehicleTerms.getTerms(), TermsDTO.class))
                        .collect(Collectors.toList())
        );
    }

    public Single<VehicleDTO> addTermsToVehicle(Long vehicleId, List<Long> termsIds){
        return Single.fromCallable(() -> {
            VehicleEntity vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

            List<TermsEntity> terms = termsRepository.findAllById(termsIds);

            for (TermsEntity term : terms) {
                VehicleTermsEntity vehicleTerms = new VehicleTermsEntity();
                vehicleTerms.setVehicle(vehicle);
                vehicleTerms.setTerms(term);

                vehicleTerms = vehicleTermsRepository.save(vehicleTerms);

                vehicle.getVehicleTerms().add(vehicleTerms);
                term.getVehicleTerms().add(vehicleTerms);
            }

            vehicleRepository.save(vehicle);
            termsRepository.saveAll(terms);

            return objectMapper.convertValue(vehicle, VehicleDTO.class);
        });
    }
}
