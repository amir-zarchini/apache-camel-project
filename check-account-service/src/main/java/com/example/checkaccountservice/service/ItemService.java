package com.example.checkaccountservice.service;

import com.example.checkaccountservice.generated.ItemRequest;
import com.example.checkaccountservice.generated.ItemResponse;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    public ItemResponse getItem(ItemRequest itemRequest){
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setId(itemRequest.getId());
        itemResponse.setCategory("Sample Account_"+itemRequest.getAccount());
        itemResponse.setName("Sample ItemName_"+itemRequest.getName());
        System.out.println("item request :" + itemRequest.getName());
        System.out.println("item request :" + itemResponse.getName());
        return itemResponse;
    }
}
