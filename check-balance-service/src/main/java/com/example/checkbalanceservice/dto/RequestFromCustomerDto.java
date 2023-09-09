package com.example.checkbalanceservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RequestFromCustomerDto implements Serializable {

    @JsonProperty("id")
    int id;

    @JsonProperty("account")
    String account;

    @JsonProperty("name")
    String name;
}
