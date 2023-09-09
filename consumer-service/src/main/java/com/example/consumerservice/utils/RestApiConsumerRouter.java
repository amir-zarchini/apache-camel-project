package com.example.consumerservice.utils;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RestApiConsumerRouter  extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            from("kafka:topic-check-balance-service")
                    .transform().simple("check balance service: ${body}\\n")
                    .to("file://target-file/reports/?fileName=report.txt&fileExist=Append")
                    .log(LoggingLevel.ERROR, " log consumer kafka ---> ${body}")
                    .to("log:my-logging-queue");
            from("kafka:topic-customer-service")
                    .transform().simple("customer service: ${body}\\n")
                    .to("file://target-file/reports/?fileName=report.txt&fileExist=Append")
                    .log(LoggingLevel.ERROR, " log consumer kafka ---> ${body}")
                    .to("log:my-logging-queue");
        }
}