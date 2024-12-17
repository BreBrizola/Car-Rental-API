package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.exception.LocationNotFoundException;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import io.reactivex.rxjava3.core.Maybe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    public void givenLocationId_whenListVehicles_thenListAllVehicles(){
        LocationEntity locationTest = new LocationEntity();
        locationTest.setId(3L);

        VehicleEntity vehicle1 = new VehicleEntity();
        VehicleEntity vehicle2 = new VehicleEntity();

        vehicle1.setId(1);
        vehicle1.setModel("model1");
        vehicle1.setPrice(12000.0);

        vehicle2.setId(2);
        vehicle2.setModel("model2");
        vehicle2.setPrice(16000.0);

        locationTest.setVehicleList(List.of(vehicle1,vehicle2));

        when(locationRepository.findById(locationTest.getId())).thenReturn(Maybe.just(locationTest));

        Maybe<List<VehicleEntity>> vehicleList = locationService.listVehicles(locationTest.getId());

        assertNotNull(vehicleList);
        verify(locationRepository, times(1)).findById(locationTest.getId());
    }

    @Test
    public void givenGetLocationByName_whenLocationDontExist_thenThrowException(){
        String locationName = "non Existing";

        when(locationRepository.findByName(locationName)).thenReturn(Maybe.empty());

        assertThrows(LocationNotFoundException.class,
                () -> locationService.getLocationByName(locationName).blockingGet());
    }

    @Test
    public void givenGetLocationById_whenLocationDontExist_thenThrowException(){
        Long locationId = 1000L;

        when(locationRepository.findById(locationId)).thenReturn(Maybe.empty());

        assertThrows(LocationNotFoundException.class,
                () -> locationService.getLocationById(locationId).blockingGet());
    }
}