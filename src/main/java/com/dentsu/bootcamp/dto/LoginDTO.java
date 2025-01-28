package com.dentsu.bootcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    @JsonProperty("id") private Long id;
    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
}
