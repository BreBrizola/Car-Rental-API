package com.dentsu.bootcamp.dto;

import com.dentsu.bootcamp.model.ReservationEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonSerialize(as = Session.class)
public class Session implements Serializable {
    private ReservationDTO reservation;
}
