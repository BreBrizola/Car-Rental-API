package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.client.WeatherClient;
import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.WeatherResponse;
import com.dentsu.bootcamp.exception.ReservationNotFoundException;
import com.dentsu.bootcamp.mapping.LocationMapper;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private WeatherClient weather;

    @Autowired
    private LocationMapper locationMapper;

    @Value("${apiKeys.weatherApiKey}")
    private String ApiKey;

    public List<LocationDTO> getAllLocations() {
        List<LocationEntity> locations = locationRepository.findAll();

        return locations.stream()
                .map(location -> locationMapper.apply(location, getLocationWeather(location)))
                .toList();
    }

    public LocationDTO getLocationById(Long id) {
        Optional<LocationEntity> locationOptional = locationRepository.findById(id);
        LocationEntity locationEntity = new LocationEntity();

        if(locationOptional.isPresent()){
            locationEntity = locationOptional.get();
        } else {throw new ReservationNotFoundException("Reservation not found");}

        LocationDTO locationDTO = locationMapper.apply(locationEntity, getLocationWeather(locationEntity));

        return locationDTO;
    }

    public WeatherResponse getLocationWeather(LocationEntity locationEntity){
        String address = locationEntity.getAddress();
        WeatherResponse weatherResponse = weather.getCurrentWeather(ApiKey, address, "no");
        return weatherResponse;
    }

    public List<VehicleEntity> listVehicles(Long id){
        Optional <LocationEntity> location = locationRepository.findById(id);
        if (location.isPresent()) {
            return location.get().getVehicleList();
        } else {
            throw new EntityNotFoundException("Location not found for the ID");
        }
    }
}
