package com.example.checkbalanceservice.service;

import com.example.checkbalanceservice.model.Request;
import com.example.checkbalanceservice.model.Response;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

//    @EndpointInject("direct:active-mq")
    @Autowired
    ProducerTemplate producer;

    @Transactional
    public Response getDirectDebit(Request request) {
        System.out.println("request" + request);
        producer.sendBody("direct:send-to-broker",request);
        return new Response();
    }

    public Response getRequestProcess(Object request) {
        System.out.println("multicast process send service: " + request);
        producer.sendBody("direct:send-to-broker",request);
        return new Response(1L,"200");
    }
}
