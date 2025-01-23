package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record AdditionalProductDTO (
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("price") double price
) implements Serializable
{
}
