package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    ReservationEntity findByConfirmationNumberAndFirstNameAndLastName(String confirmationNumber, String firstName, String lastName);

    List<ReservationEntity> findByPickupDate(LocalDate pickupDate);
}
