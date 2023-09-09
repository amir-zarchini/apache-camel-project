package com.example.checkbalanceservice.service;

import com.example.checkbalanceservice.dto.RequestFromCustomerDto;
import com.example.checkbalanceservice.model.Request;
import com.example.checkbalanceservice.model.Response;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

    @Autowired
    ProducerTemplate producer;

//    @Transactional
//    public Response getDirectDebit(Request request) {
//        System.out.println("request" + request);
//        producer.sendBody("direct:send-to-broker",request);
//        return new Response();
//    }

    public Response getRequestProcess(RequestFromCustomerDto request) {
        System.out.println("multicast process send service: " + request);
        Response response = new Response();
        producer.sendBody("direct:send-to-request-broker",request);
        if (request.getAccount().equals("1223547")) {
            response.setId(request.getId());
            response.setResponseStatus("approved!");
        } else {
            response.setId(request.getId());
            response.setResponseStatus("not approved!");
        }
        producer.sendBody("direct:send-to-response-broker", response);
        return response;
    }
}
