package com.example.checkbalanceservice.utils;

import com.example.checkbalanceservice.model.Request;
import com.example.checkbalanceservice.model.Response;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class RestApiProducerRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        //send request form direct to kafka
        from("direct:send-to-request-broker")
                .log(LoggingLevel.ERROR, " log camel ---> ${body}")
                .marshal().json(JsonLibrary.Jackson, Request.class)
                .to("kafka:topic-check-balance-service-request-from-costumer");

        //send response from direct to kafka
        from("direct:send-to-response-broker")
                .log(LoggingLevel.ERROR, " log camel ---> ${body}")
                .marshal().json(JsonLibrary.Jackson, Response.class)
                .to("kafka:topic-check-balance-service-response-to-costumer");
    }
}