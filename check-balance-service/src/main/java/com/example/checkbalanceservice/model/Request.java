package com.example.checkbalanceservice.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Request implements Serializable {

    private Long id;
    private String account;
    private String name;
}
