package com.dentsu.bootcamp.dto;

import com.dentsu.bootcamp.model.ReservationEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class Session implements Serializable {
    private ReservationDTO reservation;
}
