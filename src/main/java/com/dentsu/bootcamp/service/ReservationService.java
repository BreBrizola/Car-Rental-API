package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.dto.AdditionalProductDTO;
import com.dentsu.bootcamp.dto.ProfileDTO;
import com.dentsu.bootcamp.dto.ReservationDTO;
import com.dentsu.bootcamp.dto.Session;
import com.dentsu.bootcamp.dto.VehicleDTO;
import com.dentsu.bootcamp.exception.LocationNotFoundException;
import com.dentsu.bootcamp.exception.ReservationNotFoundException;
import com.dentsu.bootcamp.exception.VehicleNotFoundException;
import com.dentsu.bootcamp.model.AdditionalProductEntity;
import com.dentsu.bootcamp.model.ReservationEntity;
import com.dentsu.bootcamp.model.VehicleEntity;
import com.dentsu.bootcamp.repository.AdditionalProductRepository;
import com.dentsu.bootcamp.repository.LocationRepository;
import com.dentsu.bootcamp.repository.ProfileRepository;
import com.dentsu.bootcamp.repository.ReservationRepository;
import com.dentsu.bootcamp.repository.VehicleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class ReservationService {

    public static final String RESERVATION_UPDATED = "Reservation Updated";
    public static final String RESERVATION_UPDATED_STATUS = "Your reservation has been successfully updated.";
    public static final String RESERVATION_CANCELLED = "Reservation Cancelled";
    public static final String RESERVATION_CONFIRMED = "Reservation Confirmed";
    public static final String RESERVATION_CREATED_STATUS = "Your reservation has been successfully created.";

    private final ReservationRepository reservationRepository;

    private final LocationRepository locationRepository;

    private final VehicleRepository vehicleRepository;

    private final ObjectMapper objectMapper;

    private final AdditionalProductRepository additionalProductRepository;

    private final EmailService emailService;

    private final ProfileRepository profileRepository;

    private Session session;

    public ReservationService(ReservationRepository reservationRepository, LocationRepository locationRepository, VehicleRepository vehicleRepository,
                              ObjectMapper objectMapper, AdditionalProductRepository additionalProductRepository, EmailService emailService, ProfileRepository profileRepository, Session session) {
        this.reservationRepository = reservationRepository;
        this.locationRepository = locationRepository;
        this.vehicleRepository = vehicleRepository;
        this.objectMapper = objectMapper;
        this.additionalProductRepository = additionalProductRepository;
        this.emailService = emailService;
        this.profileRepository = profileRepository;
        this.session = session;
    }

    public Observable<Session> initiateReservation(ReservationEntity reservation) {
        return Observable.zip(
                Observable.fromCallable(() -> locationRepository.findById(reservation.getPickupLocation().getId())
                        .orElseThrow(() -> new LocationNotFoundException("Pickup location not found"))),
                Observable.fromCallable(() -> locationRepository.findById(reservation.getReturnLocation().getId())
                        .orElseThrow(() -> new LocationNotFoundException("Return location not found"))),
                (pickupLocation, returnLocation) -> {
                    reservation.setPickupLocation(pickupLocation);
                    reservation.setReturnLocation(returnLocation);
                    return reservation;
                }).flatMap(reservationEntity -> {
                    session.setReservation(objectMapper.convertValue(reservationEntity, ReservationDTO.class));
                    return Observable.just(session);
        });
    }

    public Observable<Session> selectCar(VehicleDTO vehicle) {
        return Observable.fromCallable(() -> {
            VehicleEntity foundVehicle = vehicleRepository.findById(vehicle.getId())
                    .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));

            ReservationDTO reservation = session.getReservation();
            reservation.setVehicle(objectMapper.convertValue(foundVehicle, VehicleDTO.class));

            session.setReservation(reservation);

            return session;
        });
    }

    public Observable<Session> extras(List<AdditionalProductDTO> additionalProducts) {
        return Observable.fromCallable(() -> {
            ReservationDTO reservation = session.getReservation();
            reservation.setAdditionalProducts(additionalProducts);
            return reservation;
        }).flatMap(reservation -> calculateTotalPrice(reservation)
                .map(totalPrice -> {
                    reservation.setTotalPrice(totalPrice);
                    session.setReservation(reservation);
                    return session;
                })
        );
    }

    public Observable<Session> commit(ReservationDTO reservation) {
        return Observable.fromCallable(() ->
                        profileRepository.findByLoyaltyNumber(reservation.getProfile().getLoyaltyNumber())
                                .orElseThrow(() -> new RuntimeException("Invalid loyalty number"))
                ).flatMap(profile -> {
                    ReservationDTO existingReservation = session.getReservation();

                    existingReservation.setProfile(objectMapper.convertValue(profile, ProfileDTO.class));
                    existingReservation.setFirstName(reservation.getFirstName());
                    existingReservation.setLastName(reservation.getLastName());
                    existingReservation.setEmail(reservation.getEmail());
                    existingReservation.setPhone(reservation.getPhone());

                    existingReservation.setConfirmationNumber(generateConfirmationNumber());

                    return Observable.fromCallable(() -> reservationRepository.save(objectMapper.convertValue(existingReservation, ReservationEntity.class) ));
                })
                .flatMap(savedReservation -> Observable.fromCallable(() -> {
                    emailService.sendMail(
                            savedReservation.getEmail(),
                            RESERVATION_CONFIRMED,
                            savedReservation.getConfirmationNumber(),
                            savedReservation.getFirstName(),
                            savedReservation.getLastName(),
                            RESERVATION_CREATED_STATUS
                    );
                    return savedReservation;
                }))
                .map(savedReservation -> {
                    session.setReservation(objectMapper.convertValue(savedReservation, ReservationDTO.class));
                    return session;
                });
    }

    public Observable<ReservationDTO> createReservation(ReservationEntity reservation) {
        return Observable.zip(
                        Observable.fromCallable(() -> profileRepository.findByLoyaltyNumber(reservation.getProfile().getLoyaltyNumber())
                                .orElseThrow(() -> new RuntimeException("Invalid loyalty number"))),
                        Observable.fromCallable(() -> locationRepository.findById(reservation.getPickupLocation().getId())
                                .orElseThrow(() -> new LocationNotFoundException("Pickup location not found"))),
                        Observable.fromCallable(() -> locationRepository.findById(reservation.getReturnLocation().getId())
                                .orElseThrow(() -> new LocationNotFoundException("Return location not found"))),
                        Observable.fromCallable(() -> vehicleRepository.findById(reservation.getVehicle().getId())
                                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"))),
                        (profile, pickupLocation, returnLocation, vehicle) -> {
                            reservation.setPickupLocation(pickupLocation);
                            reservation.setReturnLocation(returnLocation);
                            reservation.setVehicle(vehicle);
                            reservation.setProfile(profile);
                            return reservation;
                        })
                .doOnNext(reservationEntity -> reservationEntity.setConfirmationNumber(generateConfirmationNumber()))
                .flatMap(reservationEntity -> calculateTotalPrice(objectMapper.convertValue(reservationEntity, ReservationDTO.class))
                .doOnNext(reservationEntity::setTotalPrice)
                .map(totalPrice -> reservationEntity))
                .flatMap(reservationEntity -> Observable.fromCallable(() -> reservationRepository.save(reservationEntity)))
                .flatMap(reservationEntity -> {
                    emailService.sendMail(
                            reservationEntity.getEmail(),
                            RESERVATION_CONFIRMED, reservationEntity.getConfirmationNumber(),
                            reservationEntity.getFirstName(), reservationEntity.getLastName(), RESERVATION_CREATED_STATUS);

                    return Observable.just(reservationEntity);
                })
                .map(reservationEntity -> objectMapper.convertValue(reservationEntity, ReservationDTO.class));
    }

    public Single<ReservationDTO> getReservation(String confirmationNumber, String firstName, String lastName) {
        return Single.fromCallable(() -> {
            ReservationEntity reservationEntity = reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(confirmationNumber, firstName, lastName);
            validateReservationExists(reservationEntity);
            return objectMapper.convertValue(reservationEntity, ReservationDTO.class);
        });
    }

    public List<ReservationEntity> findByPickupDate(LocalDate checkInDate) {
        LocalDateTime now = LocalDateTime.now();
        String checkinTime = now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        List<ReservationEntity> reservations = reservationRepository.findByPickupDateAndPickupTime(checkInDate, checkinTime);
        return reservations;
    }

    public Single <ReservationDTO> updateReservation(String confirmationNumber, String firstName, String lastName, ReservationDTO updatedReservation) {
        return Single.fromCallable(() -> reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(confirmationNumber, firstName, lastName))
                .doOnSuccess(this::validateReservationExists)
                .flatMap(existingReservation ->
                        Single.zip(
                                Single.fromCallable(() ->
                                        updatedReservation.getPickupLocation() != null
                                                ? locationRepository.findById(updatedReservation.getPickupLocation().getId())
                                                .orElseThrow(() -> new LocationNotFoundException("Pickup location not found"))
                                                : existingReservation.getPickupLocation()
                                ),
                                Single.fromCallable(() ->
                                        updatedReservation.getReturnLocation() != null
                                                ? locationRepository.findById(updatedReservation.getReturnLocation().getId())
                                                .orElseThrow(() -> new LocationNotFoundException("Return location not found"))
                                                : existingReservation.getReturnLocation()
                                ),
                                Single.fromCallable(() ->
                                        updatedReservation.getVehicle() != null
                                                ? vehicleRepository.findById(updatedReservation.getVehicle().getId())
                                                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"))
                                                : existingReservation.getVehicle()
                                ),
                                Single.fromCallable(() ->
                                        updatedReservation.getProfile() != null
                                                ? profileRepository.findByLoyaltyNumber(updatedReservation.getProfile().getLoyaltyNumber())
                                                .orElseThrow(() -> new RuntimeException("Profile not found"))
                                                : existingReservation.getProfile()
                                ),
                                (pickupLocation, returnLocation, vehicle, profile) -> {
                                    existingReservation.setFirstName(updatedReservation.getFirstName());
                                    existingReservation.setLastName(updatedReservation.getLastName());
                                    existingReservation.setEmail(updatedReservation.getEmail());
                                    existingReservation.setPhone(updatedReservation.getPhone());
                                    existingReservation.setPickupLocation(pickupLocation);
                                    existingReservation.setPickupDate(updatedReservation.getPickupDate());
                                    existingReservation.setReturnLocation(returnLocation);
                                    existingReservation.setReturnDate(updatedReservation.getReturnDate());
                                    existingReservation.setVehicle(vehicle);
                                    existingReservation.setPickupTime(updatedReservation.getPickupTime());
                                    existingReservation.setReturnTime(updatedReservation.getReturnTime());
                                    existingReservation.setProfile(profile);

                                    return existingReservation;
                                }
                        )

                )
                .flatMap(reservation -> calculateTotalPrice(objectMapper.convertValue(reservation, ReservationDTO.class))
                        .singleOrError()
                        .map(price -> {
                            reservation.setTotalPrice(price);
                            return reservation;
                        })
                )
                .flatMap(reservation -> Single.fromCallable(() -> reservationRepository.save(reservation)))
                .flatMap(savedReservation -> {
                    emailService.sendMail(
                            savedReservation.getEmail(),
                            RESERVATION_UPDATED,
                            savedReservation.getConfirmationNumber(),
                            savedReservation.getFirstName(),
                            savedReservation.getLastName(),
                            RESERVATION_UPDATED_STATUS
                    );
                    return Single.just(objectMapper.convertValue(savedReservation, ReservationDTO.class));
                });
    }

    public Observable<Boolean> cancelReservation(String confirmationNumber, String firstName, String lastName) {
        return Observable.fromCallable(()-> reservationRepository.findByConfirmationNumberAndFirstNameAndLastName(confirmationNumber, firstName, lastName))
                .doOnNext(reservationEntity -> validateReservationExists(reservationEntity))
                .flatMap(existingReservation -> {
                    emailService.sendCancellingEmail(
                            existingReservation.getEmail(),
                            RESERVATION_CANCELLED, existingReservation.getConfirmationNumber(), existingReservation.getFirstName());
                    return Observable.just(existingReservation);
                })
                .flatMap(reservationEntity -> {
                    reservationRepository.delete(reservationEntity);
                    return Observable.just(Boolean.TRUE);
                })
                .onErrorReturnItem(Boolean.FALSE);
    }

    private String generateConfirmationNumber() {
        Random random = new Random();
        StringBuilder confirmationNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            confirmationNumber.append(random.nextInt(10));
        }
            return confirmationNumber.toString();
        }

    public Observable<Double> calculateTotalPrice(ReservationDTO reservation) {
        return Observable.zip(
                Observable.fromCallable(() -> locationRepository.findById(reservation.getPickupLocation().getId())
                        .orElseThrow(() -> new LocationNotFoundException("Pickup location not found"))),
                Observable.fromCallable(() -> locationRepository.findById(reservation.getReturnLocation().getId())
                        .orElseThrow(() -> new LocationNotFoundException("Return location not found"))),
                Observable.fromCallable(() -> vehicleRepository.findById(reservation.getVehicle().getId())
                        .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"))),
                (pickupLocation, returnLocation, vehicle) -> {
                    final double[] vehiclePrice = {vehicle.getPrice()};

                    List<Long> productList = reservation.getAdditionalProducts().stream().map(product -> product.getId()).toList();
                    Observable.fromIterable(additionalProductRepository.findAllById(productList))
                            .forEach(product -> vehiclePrice[0] += product.getPrice());

                    long rentalDuration = ChronoUnit.DAYS.between(reservation.getPickupDate(), reservation.getReturnDate());
                    double basePrice = vehiclePrice[0] * rentalDuration;

                    double pickupFee = isAfterHours(reservation.getPickupTime(), pickupLocation.getOpeningHours())
                            ? pickupLocation.getAfterHoursFeed() : 0;
                    double returnFee = isAfterHours(reservation.getReturnTime(), returnLocation.getOpeningHours())
                            ? returnLocation.getAfterHoursFeed() : 0;

                    return basePrice + pickupFee + returnFee;
                }
        );
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
