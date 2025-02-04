package com.dentsu.bootcamp.repository;

import com.dentsu.bootcamp.model.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    ReservationEntity findByConfirmationNumberAndFirstNameAndLastName(String confirmationNumber, String firstName, String lastName);

    @Query("SELECT r FROM reservation r " +
            "WHERE r.pickupDate = :pickupDate " +
            "AND r.pickupTime <= :pickupTime")
    List<ReservationEntity> findByPickupDateAndPickupTime(@Param("pickupDate") LocalDate pickupDate, @Param("pickupTime") String pickupTime);
}
