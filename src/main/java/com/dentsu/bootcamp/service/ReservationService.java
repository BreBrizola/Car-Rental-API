package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.dto.ReservationDTO;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
public class ReservationService {

    public static final String RESERVATION_UPDATED = "Reservation Updated";
    public static final String RESERVATION__UPDATED_STATUS = "Your reservation has been successfully updated.";
    public static final String RESERVATION_CANCELLED = "Reservation Cancelled";
    public static final String RESERVATION_CONFIRMED = "Reservation Confirmed";
    public static final String RESERVATION_CREATED_STATUS = "Your reservation has been successfully created.";

    private final ReservationRepository reservationRepository;

    private final LocationRepository locationRepository;

    private final VehicleRepository vehicleRepository;

    private final ObjectMapper objectMapper;

    private final AdditionalProductRepository additionalProductRepository;

    private final EmailService emailService;

    public ReservationService(ReservationRepository reservationRepository, LocationRepository locationRepository, VehicleRepository vehicleRepository,
                              ObjectMapper objectMapper, AdditionalProductRepository additionalProductRepository, EmailService emailService){
        this.reservationRepository = reservationRepository;
        this.locationRepository = locationRepository;
        this.vehicleRepository = vehicleRepository;
        this.objectMapper = objectMapper;
        this.additionalProductRepository = additionalProductRepository;
        this.emailService = emailService;
    }

    public ReservationDTO createReservation(ReservationEntity reservation) {
        LocationEntity pickupLocation = locationRepository.findById(reservation.getPickupLocation().getId()).blockingGet();
        if(pickupLocation == null){
            throw new LocationNotFoundException("Location not found");
        }

        LocationEntity returnLocation = locationRepository.findById(reservation.getReturnLocation().getId()).blockingGet();
        if(returnLocation == null){
            throw new LocationNotFoundException("Location not found");
        }

        VehicleEntity vehicle = vehicleRepository.findById(reservation.getVehicle().getId()).blockingGet();
        if(vehicle == null){
            throw new VehicleNotFoundException("Vehicle not found");
        }

        reservation.setPickupLocation(pickupLocation);
        reservation.setReturnLocation(returnLocation);
        reservation.setVehicle(vehicle);
        reservation.setConfirmationNumber(generateConfirmationNumber());
        reservation.setTotalPrice(calculateTotalPrice(reservation));

        reservationRepository.save(reservation);

        try {
            emailService.sendMail(
                    reservation.getEmail(),
                    RESERVATION_CONFIRMED, reservation.getConfirmationNumber(), reservation.getFirstName(), reservation.getLastName(), RESERVATION_CREATED_STATUS);
            log.info("email sent");

        } catch (Exception e) {
            log.error("email failed: " + e.getCause());
        }

        ReservationDTO reservationDTO = objectMapper.convertValue(reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(
                        reservation.getConfirmationNumber(),
                        reservation.getFirstName(),
                        reservation.getLastName()), ReservationDTO.class);

        return reservationDTO;
    }

    public ReservationDTO getReservation(String confirmationNumber, String firstName, String lastName) {
        ReservationEntity reservation = reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(confirmationNumber, firstName, lastName);
        validateReservationExists(reservation);
        return objectMapper.convertValue(reservation, ReservationDTO.class);
    }

    public ReservationDTO updateReservation(String confirmationNumber, String firstName, String lastName, ReservationEntity updatedReservation) {
        ReservationEntity existingReservation = reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(confirmationNumber, firstName, lastName);
        validateReservationExists(existingReservation);

        LocationEntity pickupLocation = locationRepository.findById(updatedReservation.getPickupLocation().getId()).blockingGet();

        LocationEntity returnLocation = locationRepository.findById(updatedReservation.getReturnLocation().getId()).blockingGet();

        VehicleEntity vehicle = vehicleRepository.findById(updatedReservation.getVehicle().getId()).blockingGet();


        existingReservation.setFirstName(updatedReservation.getFirstName());
        existingReservation.setLastName(updatedReservation.getLastName());
        existingReservation.setEmail(updatedReservation.getEmail());
        existingReservation.setPhone(updatedReservation.getPhone());
        existingReservation.setPickupLocation(pickupLocation);
        existingReservation.setPickupDate(updatedReservation.getPickupDate());
        existingReservation.setReturnLocation(returnLocation);
        existingReservation.setReturnDate(updatedReservation.getReturnDate());
        existingReservation.setVehicle(vehicle);
        existingReservation.setAdditionalProducts(updatedReservation.getAdditionalProducts());
        existingReservation.setPickupTime(updatedReservation.getPickupTime());
        existingReservation.setReturnTime(updatedReservation.getReturnTime());
        existingReservation.setTotalPrice(calculateTotalPrice(updatedReservation));

        try {
            emailService.sendMail(
                    existingReservation.getEmail(),
                    RESERVATION_UPDATED, existingReservation.getConfirmationNumber(), existingReservation.getFirstName(), existingReservation.getLastName(), RESERVATION__UPDATED_STATUS);
            log.info("email sent");
        } catch (Exception e) {
            log.error("email failed: " + e.getCause());
        }

        reservationRepository.save(existingReservation);

        ReservationDTO reservationDTO = objectMapper.convertValue(reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(
                existingReservation.getConfirmationNumber(),
                existingReservation.getFirstName(),
                existingReservation.getLastName()), ReservationDTO.class);

        return reservationDTO;
    }

    public void cancelReservation(String confirmationNumber, String firstName, String lastName) {
        ReservationEntity existingReservation = reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(confirmationNumber, firstName, lastName);
        validateReservationExists(existingReservation);

        try {
            emailService.sendCancellingEmail(
                    existingReservation.getEmail(),
                    RESERVATION_CANCELLED, existingReservation.getConfirmationNumber(), existingReservation.getFirstName());
            log.info("email sent");
        } catch (Exception e) {
            log.error("email failed: " + e.getCause());
        }

        reservationRepository.delete(existingReservation);
    }

    private String generateConfirmationNumber() {
        Random random = new Random();
        StringBuilder confirmationNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            confirmationNumber.append(random.nextInt(10));
        }
        return confirmationNumber.toString();
    }

    public double calculateTotalPrice(ReservationEntity reservation) {
        VehicleEntity vehicle = vehicleRepository.findById(reservation.getVehicle().getId()).blockingGet();

        LocationEntity pickupLocation = locationRepository.findById(reservation.getPickupLocation().getId()).blockingGet();

        LocationEntity returnLocation = locationRepository.findById(reservation.getReturnLocation().getId()).blockingGet();

        double vehiclePrice = vehicle.getPrice();

        for (AdditionalProductEntity item : reservation.getAdditionalProducts()) {
            AdditionalProductEntity product = additionalProductRepository.findById(item.getId()).blockingGet();
            vehiclePrice += product.getPrice();
        }
        long rentalDuration = ChronoUnit.DAYS.between(reservation.getPickupDate(), reservation.getReturnDate());
        double basePrice = vehiclePrice * rentalDuration;

        String pickupTime = reservation.getPickupTime();
        String returnTime = reservation.getReturnTime();

        String pickupOpeningHours = pickupLocation.getOpeningHours();
        String returnOpeningHours = returnLocation.getOpeningHours();

        try {
            if (isAfterHours(pickupTime, pickupOpeningHours)) {
                basePrice += reservation.getPickupLocation().getAfterHoursFeed();
            }
            if (isAfterHours(returnTime, returnOpeningHours)) {
                basePrice += reservation.getReturnLocation().getAfterHoursFeed();
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        return basePrice;
    }

    public boolean isAfterHours(String time, String openingHours) throws DateTimeParseException {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(time, timeFormatter);
        String[] hours = openingHours.split("-");
        LocalTime openingTime = LocalTime.parse(hours[0].trim(), timeFormatter);
        LocalTime closingTime = LocalTime.parse(hours[1].trim(), timeFormatter);

        return localTime.isBefore(openingTime) || localTime.isAfter(closingTime);
    }

    private void validateReservationExists(ReservationEntity reservation){
    if(Objects.isNull(reservation)){
        throw new ReservationNotFoundException("Reservation not found");
    }
    }
}
