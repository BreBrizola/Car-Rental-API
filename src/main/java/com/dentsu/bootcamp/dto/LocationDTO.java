package com.dentsu.bootcamp.dto;

import lombok.Data;

@Data
public class LocationDTO {
    private Long id;
    private String name;
    private String address;
    private String openingHours;
    private Long afterHoursFee;
    private WeatherResponse weather;
}
