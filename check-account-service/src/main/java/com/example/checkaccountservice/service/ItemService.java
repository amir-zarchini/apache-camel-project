package com.example.checkaccountservice.service;

import com.example.checkaccountservice.generated.ItemRequest;
import com.example.checkaccountservice.generated.ItemResponse;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    ProducerTemplate producer;

    public ItemResponse getItem(ItemRequest itemRequest){
        ItemResponse itemResponse = new ItemResponse();
        producer.sendBody("direct:send-to-request-broker",itemRequest);
        if (itemRequest.getAccount().equals("1223547")) {
            itemResponse.setId(itemRequest.getId());
            itemResponse.setCategory("Account Approved in soap");
            itemResponse.setName("Sample ItemName_"+itemRequest.getName());
        } else {
            itemResponse.setId(itemRequest.getId());
            itemResponse.setCategory("Account not approved! in soap!!");
            itemResponse.setName("Sample ItemName_"+itemRequest.getName());
        }
        producer.sendBody("direct:send-to-response-broker", itemResponse);
        return itemResponse;
    }
}
