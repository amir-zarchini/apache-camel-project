package com.example.customerservice.controller;

import com.example.customerservice.model.RequestMulticast;
import com.example.customerservice.model.ResponseMulticast;
import com.example.customerservice.service.RequestMultiCastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class RequestMulticastController {


    private final RequestMultiCastService requestMultiCastService;

    @PostMapping("multicast-rest")
    public ResponseMulticast getDirectDebit(@RequestBody RequestMulticast requestMultiCast) {
        return requestMultiCastService.getDirectDebit(requestMultiCast);
    }
}
