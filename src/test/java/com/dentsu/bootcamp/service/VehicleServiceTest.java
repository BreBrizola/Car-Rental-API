package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.exception.VehicleNotFoundException;
import com.dentsu.bootcamp.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    public void givenGetVehicleById_whenVehicleDontExist_thenThrowException() {
        Long vehicleId = 1000L;

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class,
                () -> vehicleService.getVehicleById(vehicleId));
    }
}