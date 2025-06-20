package com.zetexa.controller.OrdersRestController;


import com.zetexa.service.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrdersRestController {

    @Autowired
    OrdersService ordersService;


    @GetMapping("/fetch-ordeHeader-by-resellerID")
    public Object fetchOrderHeaderListByResellerID(@RequestParam("resellerID") String resellerID ,
                                                   @RequestParam("returnOverallCost") boolean returnOverallPrice,
                                                   @RequestParam("isIccidQuery") boolean isIccidQuery,
                                                   @RequestParam("isExcelRequest") boolean isExcelRequest,
                                                   @RequestParam("isZetexaPriceRequest") boolean isZetexaPriceRequest,
                                                   @RequestParam("fromDate") String fromDate,
                                                   @RequestParam("toDate") String toDate){
        Map<String,String> datesCriteria = new HashMap<>();
        datesCriteria.put("fromDate",fromDate);
        datesCriteria.put("toDate",toDate);
        return ordersService.fetchOrderHeaderListByResellerID(resellerID,returnOverallPrice,isIccidQuery,isExcelRequest,isZetexaPriceRequest,datesCriteria);
    }
}
