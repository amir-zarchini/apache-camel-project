package com.example.consumerservice.service;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    @Autowired
    ProducerTemplate producer;

    public Object getRequestProcess(Object request) {
        System.out.println("multicast process receiver service: " + request);
        return new Object();
    }
}
