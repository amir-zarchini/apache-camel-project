package com.example.customerservice.service;

import com.example.customerservice.model.RequestMulticast;
import com.example.customerservice.model.ResponseMulticast;
import org.apache.camel.*;
import org.apache.camel.builder.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestMultiCastService {

    @Autowired
    @EndpointInject(uri="direct:multi-cast")
    ProducerTemplate producer;

    @Autowired
    ConsumerTemplate consumer;

    @Autowired
    CamelContext camelContext;

    @Transactional
    public ResponseMulticast getDirectDebit(RequestMulticast requestMultiCast) {
        Exchange sendExchange = ExchangeBuilder.anExchange(camelContext).withBody(requestMultiCast).build();
        Exchange outExchange = producer.send(sendExchange);
        ResponseMulticast outString = outExchange.getMessage().getBody(ResponseMulticast.class);
        System.out.println("outString: " + outString);
        return outString;
    }
}
