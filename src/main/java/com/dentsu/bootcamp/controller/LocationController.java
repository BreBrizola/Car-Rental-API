package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.client.WeatherClient;
import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.WeatherResponse;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private WeatherClient weatherClient;

    @Value("${apiKeys.weatherApiKey}")
    private String ApiKey;

    @Operation(summary = "Retrieve all the locations")
    @GetMapping("/listAll")
    public List<LocationDTO> getAllLocations(){
        return locationService.getAllLocations();
    }

    @Operation(summary = "Retrieve a specific location, searched by Id", description = "Pass the location id (number).")
    @GetMapping("/{id}")
    public LocationDTO getLocationById(@Parameter(description = "The unique identifier of the location") @PathVariable(value = "id")Long id){
        return locationService.getLocationById(id);
    }

    @Operation(summary = "Retrieve all vehicles from a specific location, searched by Id", description = "Pass the location id (number)")
    @GetMapping("/{location_id}/vehicles")
    public List<VehicleEntity> listVehicles(@Parameter(description = "The unique identifier of the location") @PathVariable(value = "location_id")Long id){
        return locationService.listVehicles(id);
    }

    @Operation(summary = "Retrieve the current weather for a given location", description = "Pass US Zipcode, UK Postcode, Canada Postalcode, IP address, Latitude/Longitude (decimal degree) or city name.")
    @GetMapping("/weather/{location}")
    public WeatherResponse getWeather(@Parameter(description = "US Zipcode, UK Postcode, Canada Postalcode, IP address, Latitude/Longitude (decimal degree) or city name.")@PathVariable String location) {
        return weatherClient.getCurrentWeather(ApiKey, location, "no");
    }
}
