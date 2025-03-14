package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdditionalProductDTO implements Serializable
{
    @JsonProperty("id") private Long id;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private double price;
}
