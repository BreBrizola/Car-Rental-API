package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.dto.ReservationDTO;
import com.dentsu.bootcamp.exception.MissingEmailException;
import com.dentsu.bootcamp.exception.MissingNameException;
import com.dentsu.bootcamp.exception.MissingPhoneException;
import com.dentsu.bootcamp.model.ReservationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void givenReservationWithoutNames_whenCreateReservation_thenThrowMissingNameException(){
        assertThrows(MissingNameException.class, () -> reservationService.createReservation(new ReservationEntity()));
    }

    @Test
    public void givenReservationWithoutPhone_whenCreateReservation_thenThrowMissingPhoneException(){
        ReservationEntity reservationTest = new ReservationEntity();
        reservationTest.setFirstName("name");
        reservationTest.setLastName("last name");
        reservationTest.setEmail("abcd@gmail.com");
        assertThrows(MissingPhoneException.class, () -> reservationService.createReservation(reservationTest));
    }

    @Test
    public void givenReservationWithoutEmail_whenCreateReservation_thenThrowMissingEmailException(){
        ReservationEntity reservationTest = new ReservationEntity();
        reservationTest.setFirstName("name");
        reservationTest.setLastName("last name");
        assertThrows(MissingEmailException.class, () -> reservationService.createReservation(reservationTest));
    }

    @Test
     public void givenCreateReservation_whenCreateReservation_thenSaveNewReservation(){
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setFirstName("name");
        reservationEntity.setLastName("last name");
        reservationEntity.setPhone("1234567");
        reservationEntity.setEmail("abcde@gmail.com");
        reservationEntity.setPickupDate(LocalDate.now());
        reservationEntity.setReturnDate(LocalDate.now().plusDays(4));
        reservationEntity.setAdditionalProducts(Collections.emptyList());

        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setId(1);
        vehicle.setPrice(300.0);

        reservationEntity.setVehicle(vehicle);

        when(vehicleService.getVehicleById(Long.valueOf(1))).thenReturn(Optional.of(vehicle));
        when(reservationRepository.save(reservationEntity)).thenReturn(reservationEntity);
        when(reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(anyString(), anyString(), anyString()))
                .thenReturn(reservationEntity);

        ReservationDTO reservationTest = reservationService.createReservation(reservationEntity);

        assertNotNull(reservationTest);
        assertNotNull(reservationTest.getConfirmationNumber());
        assertEquals("name", reservationTest.getFirstName());
        assertEquals("last name", reservationTest.getLastName());
        assertEquals("1234567", reservationTest.getPhone());
        assertEquals("abcde@gmail.com", reservationTest.getEmail());
        assertEquals(vehicle, reservationTest.getVehicle());
    }

    @Test
    public void givenValidReservation_whenCancelReservation_thenReservationIsDeleted() {
        String confirmationNumber = "23456";
        String firstName = "name";
        String lastName = "last name";

        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setConfirmationNumber(confirmationNumber);
        reservationEntity.setFirstName(firstName);
        reservationEntity.setLastName(lastName);

        when(reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(
                confirmationNumber, firstName, lastName)).thenReturn(reservationEntity);

        reservationService.cancelReservation(confirmationNumber, firstName, lastName);

        verify(reservationRepository,times(1)).delete(reservationEntity);
        //adicionar times no verify
    }
}