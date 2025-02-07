package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.exception.LocationNotFoundException;
import com.dentsu.bootcamp.exception.ReservationNotFoundException;
import com.dentsu.bootcamp.exception.VehicleNotFoundException;
import com.dentsu.bootcamp.model.AdditionalProductEntity;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.ReservationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.AdditionalProductRepository;
import com.dentsu.bootcamp.repository.LocationRepository;
import com.dentsu.bootcamp.repository.ReservationRepository;
import com.dentsu.bootcamp.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AdditionalProductRepository additionalProductRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void givenReservationWithInvalidPickupLocation_whenCreateReservation_thenThrowLocationNotFoundException() {
        ReservationEntity reservationTest = new ReservationEntity();
        reservationTest.setPickupLocation(new LocationEntity());
        reservationTest.getPickupLocation().setId(123L);

        when(locationRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(LocationNotFoundException.class, () -> reservationService.createReservation(reservationTest).blockingFirst());
    }

    @Test
    public void givenReservationWithInvalidReturnLocation_whenCreateReservation_thenThrowLocationNotFoundException() {
        ReservationEntity reservationTest = new ReservationEntity();
        reservationTest.setPickupLocation(new LocationEntity());
        reservationTest.getPickupLocation().setId(1L);
        reservationTest.setReturnLocation(new LocationEntity());
        reservationTest.getReturnLocation().setId(456L);

        when(locationRepository.findById(1L)).thenReturn(Optional.of(new LocationEntity()));
        when(locationRepository.findById(456L)).thenReturn(Optional.empty());

        assertThrows(LocationNotFoundException.class, () -> reservationService.createReservation(reservationTest).blockingFirst());
    }

    @Test
    public void givenReservationWithInvalidVehicle_whenCreateReservation_thenThrowVehicleNotFoundException() {
        ReservationEntity reservationTest = new ReservationEntity();
        reservationTest.setPickupLocation(new LocationEntity());
        reservationTest.getPickupLocation().setId(1L);
        reservationTest.setReturnLocation(new LocationEntity());
        reservationTest.getReturnLocation().setId(2L);
        reservationTest.setVehicle(new VehicleEntity());
        reservationTest.getVehicle().setId(789L);

        when(locationRepository.findById(1L)).thenReturn(Optional.of(new LocationEntity()));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(new LocationEntity()));
        when(vehicleRepository.findById(789L)).thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class, () -> reservationService.createReservation(reservationTest).blockingFirst());
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

        Boolean b = reservationService.cancelReservation(confirmationNumber, firstName, lastName).blockingFirst();

        verify(reservationRepository,times(1)).delete(reservationEntity);
        assertTrue(b);
    }

    @Test
    public void givenCreateReservation_WhenCalculateTotalPrice_thenReturnCorrectPrice(){
        ReservationEntity reservation = new ReservationEntity();
        reservation.setPickupDate(LocalDate.now());
        reservation.setReturnDate(LocalDate.now().plusDays(3));
        reservation.setPickupTime("10:00");
        reservation.setReturnTime("14:00");
        reservation.setAdditionalProducts(Collections.emptyList());

        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setId(1L);
        vehicle.setPrice(100.0);

        LocationEntity pickupLocation = new LocationEntity();
        pickupLocation.setId(1L);
        pickupLocation.setOpeningHours("08:00 - 18:00");
        pickupLocation.setAfterHoursFeed(20L);

        LocationEntity returnLocation = new LocationEntity();
        returnLocation.setId(2L);
        returnLocation.setOpeningHours("08:00 - 18:00");
        returnLocation.setAfterHoursFeed(20L);

        reservation.setVehicle(vehicle);
        reservation.setPickupLocation(pickupLocation);
        reservation.setReturnLocation(returnLocation);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(pickupLocation));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(returnLocation));

        double totalPrice = reservationService.calculateTotalPrice(reservation).blockingFirst();

        assertEquals(300.0, totalPrice, 0.01);
    }

    @Test
    public void givenCalculateTotalPrice_WhenTimeIsOffOpeningHours_thenReturnPriceWithFeed() {
        ReservationEntity reservation = new ReservationEntity();
        reservation.setPickupDate(LocalDate.now());
        reservation.setReturnDate(LocalDate.now().plusDays(3));
        reservation.setPickupTime("03:00"); //fora do opening hours(+20 no totalPrice)
        reservation.setReturnTime("14:00");
        reservation.setAdditionalProducts(Collections.emptyList());

        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setId(1L);
        vehicle.setPrice(100.0);

        LocationEntity pickupLocation = new LocationEntity();
        pickupLocation.setId(1L);
        pickupLocation.setOpeningHours("08:00 - 18:00");
        pickupLocation.setAfterHoursFeed(20L);

        LocationEntity returnLocation = new LocationEntity();
        returnLocation.setId(2L);
        returnLocation.setOpeningHours("08:00 - 18:00");
        returnLocation.setAfterHoursFeed(20L);

        reservation.setVehicle(vehicle);
        reservation.setPickupLocation(pickupLocation);
        reservation.setReturnLocation(returnLocation);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(pickupLocation));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(returnLocation));

        double totalPrice = reservationService.calculateTotalPrice(reservation).blockingFirst();

        assertEquals(320.0, totalPrice);
    }

    @Test
    public void givenCalculateTotalPrice_WhenHasAdditionalProducts_thenReturnCorrectPrice() {
        ReservationEntity reservation = new ReservationEntity();
        reservation.setPickupDate(LocalDate.now());
        reservation.setReturnDate(LocalDate.now().plusDays(3));
        reservation.setPickupTime("10:00");
        reservation.setReturnTime("14:00");

        AdditionalProductEntity product1 = new AdditionalProductEntity();
        product1.setId(1L);
        product1.setPrice(50.0);

        AdditionalProductEntity product2 = new AdditionalProductEntity();
        product2.setId(2L);
        product2.setPrice(80.0);

        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setId(1L);
        vehicle.setPrice(100.0);

        LocationEntity pickupLocation = new LocationEntity();
        pickupLocation.setId(1L);
        pickupLocation.setOpeningHours("08:00 - 18:00");
        pickupLocation.setAfterHoursFeed(20L);

        LocationEntity returnLocation = new LocationEntity();
        returnLocation.setId(2L);
        returnLocation.setOpeningHours("08:00 - 18:00");
        returnLocation.setAfterHoursFeed(20L);

        reservation.setVehicle(vehicle);
        reservation.setPickupLocation(pickupLocation);
        reservation.setReturnLocation(returnLocation);
        reservation.setAdditionalProducts(List.of(product1, product2));

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(pickupLocation));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(returnLocation));
        when(additionalProductRepository.findAllById(anyList()))
                .thenReturn(List.of(product1, product2));

        double totalPrice = reservationService.calculateTotalPrice(reservation).blockingFirst();

        assertEquals(690.0, totalPrice);
    }

    @Test
    public void givenInvalidOpeningHoursFormat_whenCheckIfAfterHours_thenThrowDateTimeParseException() {
        String time = "10:00";
        String openingHours = "08:00 - 25:00";

        assertThrows(DateTimeParseException.class, () -> {
            reservationService.isAfterHours(time, openingHours);
        }, "Invalid Time");
    }

    @Test
    public void givenTimeIsBeforeOpeningHours_whenCheckIfAfterHours_thenReturnTrue() throws DateTimeParseException {
        String time = "07:30";
        String openingHours = "08:00 - 18:00";

        boolean result = reservationService.isAfterHours(time, openingHours);

        assertTrue(result);
    }

    @Test
    public void givenTimeIsAfterOpeningHours_whenCheckIfAfterHours_thenReturnTrue() throws DateTimeParseException {
        String time = "20:00";
        String openingHours = "08:00 - 18:00";

        boolean result = reservationService.isAfterHours(time, openingHours);

        assertTrue(result);
    }

    @Test
    public void givenTimeBetweenOpeningHours_whenCheckIfAfterHours_thenReturnFalse() throws DateTimeParseException {
        String time = "15:00";
        String openingHours = "08:00 - 18:00";

        boolean result = reservationService.isAfterHours(time, openingHours);

        assertFalse(result);
    }

    @Test
    public void givenInvalidDetails_whenGetReservation_thenThrowReservationNotFoundException() {
        when(reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(
                "000000", "Name", "Last Name")).thenReturn(null);

        assertThrows(ReservationNotFoundException.class, () ->
                reservationService.getReservation("000000", "Name", "Last Name").blockingFirst());
    }
}