package com.example.checkbalanceservice.controller;

import com.example.checkbalanceservice.dto.RequestFromCustomerDto;
import com.example.checkbalanceservice.model.Request;
import com.example.checkbalanceservice.model.Response;
import com.example.checkbalanceservice.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class RequestController {

    private final RequestService requestService;

//    @PostMapping("direct-debit")
//    public Response getDirectDebit(@RequestBody Request request) {
//        return requestService.getDirectDebit(request);
//    }

    @PostMapping("receive-from-multiCastService")
    public Response getProcessMulticastRequest(@RequestBody RequestFromCustomerDto request) {
        return requestService.getRequestProcess(request);
    }
}
