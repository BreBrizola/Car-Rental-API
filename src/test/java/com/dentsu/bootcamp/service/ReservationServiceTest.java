package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.exception.MissingEmailException;
import com.dentsu.bootcamp.exception.MissingNameException;
import com.dentsu.bootcamp.exception.MissingPhoneException;
import com.dentsu.bootcamp.model.AdditionalProductEntity;
import com.dentsu.bootcamp.model.LocationEntity;
import com.dentsu.bootcamp.model.ReservationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.LocationRepository;
import com.dentsu.bootcamp.repository.ReservationRepository;
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
    private VehicleService vehicleService;

    @Mock
    private AdditionalProductService additionalProductService;

    //@Mock
    //private LocationService locationService;

    //@Mock
    //private LocationMapper locationMapper;

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


    /* Metodo falhando, verificar porque
    @Test
     public void givenCreateReservation_whenCreateReservation_thenSaveNewReservation(){
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setFirstName("name");
        reservationEntity.setLastName("last name");
        reservationEntity.setPhone("1234567");
        reservationEntity.setEmail("abcde@gmail.com");
        reservationEntity.setPickupTime("13:00");
        reservationEntity.setReturnTime("15:00");
        reservationEntity.setPickupDate(LocalDate.now());
        reservationEntity.setReturnDate(LocalDate.now().plusDays(4));
        reservationEntity.setAdditionalProducts(Collections.emptyList());

        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setId(1);
        vehicle.setPrice(300.0);

        reservationEntity.setVehicle(vehicle);

        LocationEntity location = new LocationEntity();
        location.setId(1L);
        location.setName("Location 1");
        location.setAddress("Rua X");
        location.setOpeningHours("9:00 - 19:00");
        location.setAfterHoursFeed(15L);

        reservationEntity.setPickupLocation(location);
        reservationEntity.setReturnLocation(location);

        when(vehicleService.getVehicleById(Long.valueOf(1))).thenReturn(Optional.of(vehicle));
        when(locationService.getLocationById(Long.valueOf(1))).thenReturn(locationMapper.apply(location));
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
     */

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

        when(vehicleService.getVehicleById(1L)).thenReturn(Optional.of(vehicle));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(pickupLocation));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(returnLocation));

        double totalPrice = reservationService.calculateTotalPrice(reservation);

        assertEquals(300.0, totalPrice, 0.01);
    }

    @Test
    public void givenCalculateTotalPrice_WhenTimeIsOffOpeningHours_thenReturnPriceWithFeed(){
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

        when(vehicleService.getVehicleById(1L)).thenReturn(Optional.of(vehicle));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(pickupLocation));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(returnLocation));

        double totalPrice = reservationService.calculateTotalPrice(reservation);

        assertEquals(320.0, totalPrice);
    }

    @Test
    public void givenCalculateTotalPrice_WhenHasAdditionalProducts_thenReturnCorrectPrice(){
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
        reservation.setAdditionalProducts(List.of(product1,product2));

        when(vehicleService.getVehicleById(1L)).thenReturn(Optional.of(vehicle));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(pickupLocation));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(returnLocation));
        when(additionalProductService.getAdditionProducts(1L)).thenReturn(product1);
        when(additionalProductService.getAdditionProducts(2L)).thenReturn(product2);

        double totalPrice = reservationService.calculateTotalPrice(reservation);

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

}