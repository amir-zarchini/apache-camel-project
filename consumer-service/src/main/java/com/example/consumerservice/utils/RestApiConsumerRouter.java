package com.example.consumerservice.utils;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RestApiConsumerRouter  extends RouteBuilder {

        @Override
        public void configure() throws Exception {

            // consume topic from kafka for request from customerService in checkBalanceService
            from("kafka:topic-check-balance-service-request-from-costumer")
                    .transform().simple("check balance service " +
                            "request from costumer: ${body}\\n")
                    .to("file://target-file/reports/?fileName=report.txt&fileExist=Append")
                    .log(LoggingLevel.ERROR, " log consumer kafka ---> ${body}")
                    .to("log:my-logging-queue");

            //consume topic from kafka for response to customerService in checkBalanceService
            from("kafka:topic-check-balance-service-response-to-costumer")
                    .transform().simple("check balance service " +
                            "response to costumer: ${body}\\n")
                    .to("file://target-file/reports/?fileName=report.txt&fileExist=Append")
                    .log(LoggingLevel.ERROR, " log consumer kafka ---> ${body}")
                    .to("log:my-logging-queue");

            //consume topic from kafka for request from customerService in checkAccountService
            from("kafka:topic-soap-request-customer-service")
                    .transform().simple("soap request from " +
                            "customerService to checkAccountService: ${body}\\n")
                    .to("file://target-file/reports/?fileName=report.txt&fileExist=Append")
                    .log(LoggingLevel.ERROR, " log consumer kafka ---> ${body}")
                    .to("log:my-logging-queue");

            //consume topic from kafka for response to customerService in checkAccountService
            from("kafka:topic-soap-response-customer-service")
                    .transform().simple("soap response to " +
                            "customerService from checkAccountService: ${body}\\n")
                    .to("file://target-file/reports/?fileName=report.txt&fileExist=Append")
                    .log(LoggingLevel.ERROR, " log consumer kafka ---> ${body}")
                    .to("log:my-logging-queue");
        }
}