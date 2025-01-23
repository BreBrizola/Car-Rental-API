package com.dentsu.bootcamp.controller;

import com.dentsu.bootcamp.dto.ReservationDTO;
import com.dentsu.bootcamp.dto.Session;
import com.dentsu.bootcamp.model.AdditionalProductEntity;
import com.dentsu.bootcamp.model.ReservationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.service.ReservationService;
import io.reactivex.rxjava3.core.Observable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }


    @Operation(summary = "Create a new reservation", description = "Submit the required details including first and last name, email, phone number, pickup and return dates, pickup and return locations, and vehicle information. Additional products can be included optionally.")
    @PostMapping("/create")
    public Observable<ReservationDTO> createReservation(@RequestBody @Valid ReservationEntity reservation) {
        return reservationService.createReservation(reservation);
    }

    @PostMapping("/iniciate")
    public Observable<Session> iniciateReservation(@RequestBody ReservationEntity reservation){
        return reservationService.initiateReservation(reservation);
    }

    @PostMapping("/selectCar")
    public Observable<Session> selectCar(@RequestBody VehicleEntity vehicle){
        return reservationService.selectCar(vehicle);
    }

    @PostMapping("/extras")
    public Observable<Session> extras(@RequestBody List<AdditionalProductEntity> additionalProducts){
        return reservationService.extras(additionalProducts);
    }

    @Operation(summary = "Retrieve information from a specific reservation", description = "Pass the confirmation number, first and last name.")
    @GetMapping("/retrieve")
    public Observable<ReservationDTO> getReservation(@Parameter(description = "The unique confirmation number of the reservation.") @RequestParam String confirmationNumber,
                                                    @Parameter(description = "The first name of the customer associated with the reservation.")@RequestParam String firstName,
                                                    @Parameter(description = "The last name of the customer associated with the reservation.")@RequestParam String lastName) {
        return reservationService.getReservation(confirmationNumber, firstName, lastName);
    }

    @Operation(summary = "Update the information from a specific reservation", description = "Pass the confirmation number, first and last name.")
    @PutMapping("/update/{confirmationNumber}")
    public Observable <ReservationDTO> updateReservation(@Parameter(description = "The unique confirmation number of the reservation.") @PathVariable String confirmationNumber,
                                               @Parameter(description = "The first name of the customer associated with the reservation.") @RequestParam String firstName,
                                               @Parameter(description = "The last name of the customer associated with the reservation.")@RequestParam String lastName,
                                               @RequestBody @Valid ReservationEntity updatedReservation) {
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
