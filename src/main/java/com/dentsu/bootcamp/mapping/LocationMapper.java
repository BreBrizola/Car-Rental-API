package com.dentsu.bootcamp.mapping;

import com.dentsu.bootcamp.dto.LocationDTO;
import com.dentsu.bootcamp.dto.WeatherResponse;
import com.dentsu.bootcamp.model.LocationEntity;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public LocationDTO apply(LocationEntity locationEntity, WeatherResponse weather) {
        LocationDTO locationDto = new LocationDTO();

        locationDto.setId(locationEntity.getId());
        locationDto.setName(locationEntity.getName());
        locationDto.setAddress(locationEntity.getAddress());
        locationDto.setOpeningHours(locationEntity.getOpeningHours());
        locationDto.setAfterHoursFee(locationEntity.getAfterHoursFeed());
        locationDto.setWeather(weather);

        return locationDto;
    }

    public LocationDTO apply(LocationEntity locationEntity) {
        LocationDTO locationDto = new LocationDTO();

        locationDto.setId(locationEntity.getId());
        locationDto.setName(locationEntity.getName());
        locationDto.setAddress(locationEntity.getAddress());
        locationDto.setOpeningHours(locationEntity.getOpeningHours());
        locationDto.setAfterHoursFee(locationEntity.getAfterHoursFeed());

        return locationDto;
    }

}
