package com.example.checkaccountservice.config;

import com.example.checkaccountservice.generated.ItemRequest;
import com.example.checkaccountservice.generated.ItemResponse;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class SoapCheckAccountRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        //send request form direct to kafka
        from("direct:send-to-request-broker")
                .log(LoggingLevel.ERROR, " log camel ---> ${body}")
                .marshal().json(JsonLibrary.Jackson, ItemRequest.class)
                .to("kafka:topic-soap-request-customer-service");

        //send response from direct to kafka
        from("direct:send-to-response-broker")
                .log(LoggingLevel.ERROR, " log camel ---> ${body}")
                .marshal().json(JsonLibrary.Jackson, ItemResponse.class)
                .to("kafka:topic-soap-response-customer-service");
    }
}
