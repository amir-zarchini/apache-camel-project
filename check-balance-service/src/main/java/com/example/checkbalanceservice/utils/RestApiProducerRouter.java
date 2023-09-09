package com.example.checkbalanceservice.utils;

import com.example.checkbalanceservice.model.Request;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class RestApiProducerRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:send-to-broker")
                .log(LoggingLevel.ERROR, " log camel ---> ${body}")
                .marshal().json(JsonLibrary.Jackson, Request.class)
                .to("kafka:topic-check-balance-service");
    }
}



//        JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Request.class);
//
//        from("direct:rabbit-mq").id("rabbitMQRoute").marshal(jsonDataFormat)
//                .to("rabbitmq://localhost:15672/TEST-QUEUE.exchange?queue=queue&autoDelete=false")
//                .end();

//    from("direct:rabbit-mq")
////                .process(this::getRequestDto)
//                .log(LoggingLevel.ERROR, " log camel ---> ${body}")
//                .marshal().json(JsonLibrary.Jackson, Request.class)
//                .to("log:request")
//        .to(ExchangePattern.InOut, "spring-rabbitmq:default?routingKey=def-rout")
//                .to("log:response");
//
//}
