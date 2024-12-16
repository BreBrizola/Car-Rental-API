package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.client.WeatherClient;
import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.WeatherResponse;
import com.dentsu.bootcamp.exception.LocationNotFoundException;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
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

    public Flowable<LocationDTO> getAllLocations() {
        return locationRepository.findAll()
                .map(location -> objectMapper.convertValue(location, LocationDTO.class));
    }

    public Maybe<LocationDTO> getLocationById(Long id) {
        return locationRepository.findById(id)
                .map(location -> objectMapper.convertValue(location, LocationDTO.class))
                .switchIfEmpty(Maybe.error(new LocationNotFoundException("Location not found")));
    }

    @Cacheable("locationsByName")
    public Maybe<LocationDTO> getLocationByName(String name){
        return locationRepository.findByName(name)
                .map(location -> objectMapper.convertValue(location, LocationDTO.class))
                .switchIfEmpty(Maybe.error(new LocationNotFoundException("Location not found")));
    }

    public WeatherResponse getLocationWeather(LocationEntity locationEntity){
        String address = locationEntity.getAddress();
        WeatherResponse weatherResponse = weather.getCurrentWeather(apiKey, address, "no");
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
