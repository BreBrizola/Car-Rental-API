package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.client.WeatherClient;
import com.dentsu.bootcamp.dto.AdditionalProductDTO;
import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.dto.WeatherResponse;
import com.dentsu.bootcamp.exception.LocationNotFoundException;
import com.dentsu.bootcamp.exception.ReservationNotFoundException;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private WeatherClient weather;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${apiKeys.weatherApiKey}")
    private String ApiKey;

    public Flowable<LocationDTO> getAllLocations() {
        return locationRepository.findAll()
                .map(location -> objectMapper.convertValue(location, LocationDTO.class));
    }

    public Maybe<LocationDTO> getLocationById(Long id) {
        return locationRepository.findById(id)
                .map(location -> objectMapper.convertValue(location, LocationDTO.class));
    }

    @Cacheable("locationsByName")
    public Maybe<LocationDTO> getLocationByName(String name){
        return locationRepository.findByName(name)
                .map(location -> objectMapper.convertValue(location, LocationDTO.class));
    }

    public WeatherResponse getLocationWeather(LocationEntity locationEntity){
        String address = locationEntity.getAddress();
        WeatherResponse weatherResponse = weather.getCurrentWeather(ApiKey, address, "no");
        return weatherResponse;
    }

    public Maybe<List<VehicleEntity>> listVehicles(Long id){
        return locationRepository.findById(id)
                .map(location -> {
                    if (location != null) {
                        return location.getVehicleList();
                    } else {
                        throw new EntityNotFoundException("Location not found for the ID");
                    }
                });
    }
}
