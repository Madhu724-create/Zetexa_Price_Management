package com.zetexa.controller.ResellerController;


import com.zetexa.entity.Reseller.Reseller;
import com.zetexa.service.reseller.ResellerCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reseller")
public class ResellerRestController {


    @Autowired
    ResellerCustomerService resellerCustomerService;

    @GetMapping(value = "/fetch-reseller-customer-list")
    public List<UUID> getAllResellerCustomerList(){
        return resellerCustomerService.getAllResellerCustomerList();
    }

    @GetMapping(value = "/fetchResellerByResellerID")
    public Reseller fetchResellerListByResellerID(@RequestParam("resellerID") String resellerID){
        return resellerCustomerService.fetchResellerListByResellerID(resellerID);
    }
}
