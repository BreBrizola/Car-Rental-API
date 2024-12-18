package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.client.WeatherRetroFitClient;
import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.WeatherResponse;
import com.dentsu.bootcamp.exception.LocationNotFoundException;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    private final WeatherRetroFitClient weatherRetroFitClient;

    private final ObjectMapper objectMapper;

    private String apiKey;

    public LocationService(LocationRepository locationRepository, WeatherRetroFitClient weatherRetroFitClient, ObjectMapper objectMapper, @Value("${apiKeys.weatherApiKey}") String apiKey){
        this.locationRepository = locationRepository;
        this.weatherRetroFitClient = weatherRetroFitClient;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public Observable<List<LocationDTO>> getAllLocations() {
        return Observable.fromCallable(() -> locationRepository.findAll())
                .flatMap(list -> Observable.fromIterable(list)
                .map(location -> objectMapper.convertValue(location, LocationDTO.class))
                .toList()
                .toObservable());
    }

    public Observable<LocationDTO> getLocationById(Long id) {
        return Observable.fromCallable(() -> locationRepository.findById(id))
                .flatMap(optinalLocation -> optinalLocation
                .map(location -> Observable.just(objectMapper.convertValue(location, LocationDTO.class)))
                .orElse(Observable.error(new LocationNotFoundException("Location not found"))));
    }

    @Cacheable("locationsByName")
    public Observable<LocationDTO> getLocationByName(String name){
        return Observable.fromCallable(() -> locationRepository.findByName(name))
                .flatMap(optinalLocation -> optinalLocation
                .map(location -> Observable.just(objectMapper.convertValue(location, LocationDTO.class)))
                .orElse(Observable.error(new LocationNotFoundException("Location not found"))));
    }

    public Observable<WeatherResponse> getLocationWeather(LocationEntity locationEntity){
        String address = locationEntity.getAddress();
        Observable<WeatherResponse> weatherResponse = weatherRetroFitClient.getCurrentWeather(apiKey, address, "no");
        return weatherResponse;
    }

    public Observable<List<VehicleEntity>> listVehicles(Long id){
        return Observable.fromCallable(() -> locationRepository.findById(id))
                .flatMap(optinalLocation -> optinalLocation
                .map(location -> Observable.fromIterable(location.getVehicleList())
                .toList()
                .toObservable())
                .orElse(Observable.error(new LocationNotFoundException("Location not found"))));
                }
    }