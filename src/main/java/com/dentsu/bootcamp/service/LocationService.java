package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.client.WeatherRetroFitClient;
import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.dto.WeatherResponse;
import com.dentsu.bootcamp.exception.LocationNotFoundException;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
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
        this.weatherRetroFitClient = weatherRetroFitClient; //
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public Observable<List<LocationDTO>> getAllLocations() {
        return Observable.fromCallable(() -> locationRepository.findAll())
                .map(location -> convertToDTO(location));
    }

    public List<LocationDTO> convertToDTO(List<LocationEntity> list){
        return list.stream()
                .map(location -> objectMapper.convertValue(location, LocationDTO.class))
                .toList();
    }

    public Observable<LocationDTO> getLocationById(Long id) {
        return Observable.fromCallable(() -> locationRepository.findById(id))
                .map(location -> objectMapper.convertValue(location, LocationDTO.class));
    }

    //@Cacheable("locationsByName")
    public Observable<LocationDTO> getLocationByName(String name){
        return Observable.fromCallable(() -> locationRepository.findByName(name))
                .map(location -> objectMapper.convertValue(location, LocationDTO.class));
    }

    public Observable<WeatherResponse> getLocationWeather(LocationEntity locationEntity){
        String address = locationEntity.getAddress();
        Observable<WeatherResponse> weatherResponse = weatherRetroFitClient.getCurrentWeather(apiKey, address, "no");
        return weatherResponse;
    }

    public Observable<List<VehicleEntity>> listVehicles(Long id){
        return Observable.fromCallable(() -> locationRepository.findById(id))
                .flatMap(locationOptional -> locationOptional
                .map(location -> Observable.just(location.getVehicleList()))
                .orElse(Observable.error(new LocationNotFoundException("Location not found"))));
                }
    }//Não deu pra usar location.getVehicleList direto por ele ser optinal quando é encontrado pelo repository