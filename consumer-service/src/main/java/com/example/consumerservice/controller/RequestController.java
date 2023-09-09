package com.example.consumerservice.controller;

import com.example.consumerservice.service.RequestService;
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

    @PostMapping("receive-from-multiCastService")
    public Object getProcessMulticastRequest(@RequestBody Object request) {
        return requestService.getRequestProcess(request);
    }
}
