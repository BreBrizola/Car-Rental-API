package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.TermsDTO;
import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.exception.LocationNotFoundException;
import com.dentsu.bootcamp.exception.VehicleNotFoundException;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.LocationTermsEntity;
import com.dentsu.bootcamp.model.TermsEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.model.VehicleTermsEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import com.dentsu.bootcamp.repository.LocationTermsRepository;
import com.dentsu.bootcamp.repository.TermsRepository;
import com.dentsu.bootcamp.repository.VehicleRepository;
import com.dentsu.bootcamp.repository.VehicleTermsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TermsService {
    private VehicleTermsRepository vehicleTermsRepository;
    private ObjectMapper objectMapper;
    private VehicleRepository vehicleRepository;
    private TermsRepository termsRepository;
    private LocationRepository locationRepository;
    private LocationTermsRepository locationTermsRepository;

    public TermsService (VehicleTermsRepository vehicleTermsRepository, ObjectMapper objectMapper, VehicleRepository vehicleRepository,
                         TermsRepository termsRepository, LocationRepository locationRepository, LocationTermsRepository locationTermsRepository){
        this.vehicleTermsRepository = vehicleTermsRepository;
        this.objectMapper = objectMapper;
        this.vehicleRepository = vehicleRepository;
        this.termsRepository = termsRepository;
        this.locationRepository = locationRepository;
        this.locationTermsRepository = locationTermsRepository;
    }

    public Single<List<TermsDTO>> getVehicleTerms(VehicleDTO vehicle){
        return Single.fromCallable(() ->
                vehicleTermsRepository.findByVehicle(objectMapper.convertValue(vehicle, VehicleEntity.class)).stream()
                        .map(vehicleTerms -> objectMapper.convertValue(vehicleTerms.getTerms(), TermsDTO.class))
                        .collect(Collectors.toList())
        );
    }

    public Single<List<TermsDTO>> getLocationTerms(LocationDTO location){
        return Single.fromCallable(() ->
                locationTermsRepository.findByLocation(objectMapper.convertValue(location, LocationEntity.class)).stream()
                        .map(locationTerms -> objectMapper.convertValue(locationTerms.getTerms(), TermsDTO.class))
                        .collect(Collectors.toList())
        );
    }

    public Single<VehicleDTO> addTermsToVehicle(Long vehicleId, List<Long> termsIds){
        return Single.fromCallable(() -> {
            VehicleEntity vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));

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
            System.out.println(vehicle.getVehicleTerms());

            return objectMapper.convertValue(vehicle, VehicleDTO.class);
        });
    }

    public Single<LocationDTO> addTermsToLocation(Long locationId, List<Long> termsIds){
        return Single.fromCallable(() -> {
            LocationEntity location = locationRepository.findById(locationId)
                    .orElseThrow(() -> new LocationNotFoundException("Location not found"));

            List<TermsEntity> terms = termsRepository.findAllById(termsIds);

            for (TermsEntity term : terms) {
                LocationTermsEntity locationTerms = new LocationTermsEntity();
                locationTerms.setLocation(location);
                locationTerms.setTerms(term);

                locationTerms = locationTermsRepository.save(locationTerms);

                location.getLocationTerms().add(locationTerms);
                term.getLocationTerms().add(locationTerms);
            }

            locationRepository.save(location);
            termsRepository.saveAll(terms);
            System.out.println(location.getLocationTerms());

            return objectMapper.convertValue(location, LocationDTO.class);
        });
    }
}
