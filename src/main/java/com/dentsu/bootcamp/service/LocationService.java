package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.client.WeatherClient;
import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.dto.WeatherResponse;
import com.dentsu.bootcamp.exception.LocationNotFoundException;
import com.dentsu.bootcamp.exception.VehicleNotFoundException;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    private final WeatherClient weather;

    private final ObjectMapper objectMapper;

    private String apiKey;

    public LocationService(LocationRepository locationRepository, WeatherClient weather,ObjectMapper objectMapper, @Value("${apiKeys.weatherApiKey}") String apiKey){
        this.locationRepository = locationRepository;
        this.weather = weather;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public Observable<List<LocationDTO>> getAllLocations() {
        return Observable.fromCallable(() -> locationRepository.findAll()
                .stream()
                .map(location -> objectMapper.convertValue(location, LocationDTO.class))
                .toList());
    }

    public Observable<LocationDTO> getLocationById(Long id) {
        return Observable.fromCallable(() -> locationRepository.findById(id)
                .map(location -> objectMapper.convertValue(location, LocationDTO.class))
                .orElseThrow(() -> new LocationNotFoundException("Location not found")));
    }

    @Cacheable("locationsByName")
    public Observable<LocationDTO> getLocationByName(String name){
        return Observable.fromCallable(() -> locationRepository.findByName(name)
                .map(location -> objectMapper.convertValue(location, LocationDTO.class))
                .orElseThrow(() -> new LocationNotFoundException("Location not found")));
    }

    public WeatherResponse getLocationWeather(LocationEntity locationEntity){
        String address = locationEntity.getAddress();
        WeatherResponse weatherResponse = weather.getCurrentWeather(apiKey, address, "no");
        return weatherResponse;
    }

    public Observable<List<VehicleEntity>> listVehicles(Long id){
        return Observable.fromCallable(() -> locationRepository.findById(id)
                .map(location -> location.getVehicleList())
                .orElseThrow(() -> new LocationNotFoundException("Location not found")));
                }
    }
