package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.dto.AdditionalProductDTO;
import com.dentsu.bootcamp.dto.ReservationDTO;
import com.dentsu.bootcamp.dto.Session;
import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.model.ReservationEntity;
import com.dentsu.bootcamp.service.ReservationService;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @Operation(summary = "Initiates the car rental process", description = "Pass the pickupTime, pickupDate, pickupLocation, returnTime, returnDate and returnLocation.")
    @PostMapping("/iniciate")
    public Observable<Session> iniciateReservation(@RequestBody ReservationEntity reservation){
        return reservationService.initiateReservation(reservation);
    }

    @Operation(summary = "Selects a vehicle for the reservation", description = "Pass the selected vehicle(id, model and price). No need to pass the terms.")
    @PostMapping("/selectCar")
    public Observable<Session> selectCar(@RequestBody VehicleDTO vehicle) {
        return reservationService.selectCar(vehicle);
    }

    @Operation(summary = "Adds additional products to the reservation", description = "Pass the desired additional products, they are optional")
    @PostMapping("/extras")
    public Observable<Session> extras(@RequestBody List<AdditionalProductDTO> additionalProducts) {
        return reservationService.extras(additionalProducts);
    }

    @Operation(summary = "Confirms the reservation", description = "Pass customer information (first name, last name, email, phone and profile)")
    @PostMapping("/commit")
    public Observable<Session> commit(@RequestBody ReservationDTO reservation) {
        return reservationService.commit(reservation);
    }

    @Operation(summary = "Retrieve information from a specific reservation", description = "Pass the confirmation number, first and last name.")
    @GetMapping("/retrieve")
    public Single<ReservationDTO> getReservation(@Parameter(description = "The unique confirmation number of the reservation.") @RequestParam String confirmationNumber,
                                                 @Parameter(description = "The first name of the customer associated with the reservation.")@RequestParam String firstName,
                                                 @Parameter(description = "The last name of the customer associated with the reservation.")@RequestParam String lastName) {
        return reservationService.getReservation(confirmationNumber, firstName, lastName);
    }

    @Operation(summary = "Update the information from a specific reservation", description = "Pass the confirmation number, first and last name.")
    @PutMapping("/update/{confirmationNumber}")
    public Single<ReservationDTO> updateReservation(@Parameter(description = "The unique confirmation number of the reservation.") @PathVariable String confirmationNumber,
                                               @Parameter(description = "The first name of the customer associated with the reservation.") @RequestParam String firstName,
                                               @Parameter(description = "The last name of the customer associated with the reservation.")@RequestParam String lastName,
                                               @RequestBody @Valid ReservationDTO updatedReservation) {
        return reservationService.updateReservation(confirmationNumber, firstName, lastName, updatedReservation);
    }

    @Operation(summary = "Cancel a specific reservation", description = "Pass the confirmation number, first and last name.")
    @DeleteMapping("/cancel")
    public Observable<Boolean> cancelReservation(@Parameter(description = "The unique confirmation number of the reservation.") @RequestParam String confirmationNumber,
                                  @Parameter(description = "The first name of the customer associated with the reservation.") @RequestParam String firstName,
                                  @Parameter(description = "The last name of the customer associated with the reservation.") @RequestParam String lastName) {
        return reservationService.cancelReservation(confirmationNumber, firstName, lastName);
    }
}
