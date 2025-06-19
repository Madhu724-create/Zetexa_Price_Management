package com.zetexa.controller.sftp;


import com.zetexa.entity.Orders.ESimUsageHistory;
import com.zetexa.Pojo.ESimDetailsResponsePojo;
import com.zetexa.service.sftp.ESimUsageWithICCIDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/esim-usage")
public class ESimUsageWithICCIDController {

    @Autowired
    ESimUsageWithICCIDService eSimUsageWithICCIDService;

    @GetMapping("/group-by-iccid")
    public ResponseEntity<?> getESimUsageHistoryWithGroupByICCID(@RequestParam int pageNumber , @RequestParam int pageSize){
        try {
            Map<String, List<ESimUsageHistory>> response = eSimUsageWithICCIDService.getSFTPDetailsWithGroupByICCID(pageNumber,pageSize);
            return new ResponseEntity<>(prepareESimDetailsWithICCIDResponse(response,200,"Successfully fetched the eSimUsage details", true), HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(prepareESimDetailsWithICCIDResponse(exception,500,"Exception Occurred while Fetching eSimUsageDetails", false), HttpStatus.OK);
        }
    }

    private ESimDetailsResponsePojo prepareESimDetailsWithICCIDResponse(Object response, int statusCode, String message, boolean isSuccess) {
        return new ESimDetailsResponsePojo(message,statusCode,response,isSuccess);
    }
}
