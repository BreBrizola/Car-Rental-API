package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.client.WeatherRetroFitClient;
import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.WeatherResponse;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.service.LocationService;
import io.reactivex.rxjava3.core.Observable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("location")
public class LocationController {

    @Resource
    private LocationService locationService;

    private WeatherRetroFitClient weatherRetroFitClient;

    private String apiKey;

    public LocationController(LocationService locationService,
                              WeatherRetroFitClient weatherRetroFitClient,
                              @Value("${apiKeys.weatherApiKey}") String apiKey) {
        this.locationService = locationService;
        this.weatherRetroFitClient = weatherRetroFitClient;
        this.apiKey = apiKey;
    }


    @Operation(summary = "Retrieve all the locations")
    @GetMapping("/listAll")
    public Observable<List<LocationDTO>> getAllLocations() {
        return locationService.getAllLocations();
    }

    @Operation(summary = "Retrieve a specific location, searched by Id", description = "Pass the location id (number).")
    @GetMapping("/id/{id}")
    public Observable<LocationDTO> getLocationById(@Parameter(description = "The unique identifier of the location") @PathVariable(value = "id")Long id){
        return locationService.getLocationById(id);
    }

    @Operation(summary = "Retrieve all vehicles from a specific location, searched by Id", description = "Pass the location id (number)")
    @GetMapping("/{location_id}/vehicles")
    public Observable<List<VehicleEntity>> listVehicles(@Parameter(description = "The unique identifier of the location") @PathVariable(value = "location_id")Long id){
        return locationService.listVehicles(id);
    }

    @Operation(summary = "Retrieve the current weather for a given location", description = "Pass US Zipcode, UK Postcode, Canada Postalcode, IP address, Latitude/Longitude (decimal degree) or city name.")
    @GetMapping("/weather/{location}")
    public Observable<WeatherResponse> getWeather(@Parameter(description = "US Zipcode, UK Postcode, Canada Postalcode, IP address, Latitude/Longitude (decimal degree) or city name.")@PathVariable String location) {
        return weatherRetroFitClient.getCurrentWeather(apiKey, location, "no");
    }

    @Operation(summary = "Retrieve a specific location, searched by name", description = "Pass the location name(String).")
    @GetMapping("/name/{name}")
    public Observable<LocationDTO> getLocationByName(@Parameter(description = "The name of the location") @PathVariable(value = "name")String name){
        return locationService.getLocationByName(name);
    }

}
