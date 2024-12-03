package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        when(locationRepository.findById(locationTest.getId())).thenReturn(Optional.of(locationTest));

        List<VehicleEntity> vehicleList = locationService.listVehicles(locationTest.getId());

        assertNotNull(vehicleList);
        verify(locationRepository, times(1)).findById(locationTest.getId());
    }
}