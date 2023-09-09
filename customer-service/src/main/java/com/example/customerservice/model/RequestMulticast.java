package com.example.customerservice.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestMulticast implements Serializable {

    private int id;
    private String account;
    private String name;
}