package com.zetexa.service.reseller;


import com.zetexa.entity.Reseller.Reseller;
import com.zetexa.repository.reseller.ResellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResellerCustomerService {

    @Autowired
    ResellerRepository resellerRepository;

    public List<UUID> getAllResellerCustomerList() {
        return resellerRepository.findAll().stream().map(Reseller::getResellerId).toList();
    }

    public Reseller fetchResellerListByResellerID(String resellerID) {
        return resellerRepository.findById(Long.valueOf(resellerID)).get();
    }
}
