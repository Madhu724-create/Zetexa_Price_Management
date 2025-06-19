package com.zetexa.controller.OrdersRestController;


import com.zetexa.service.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrdersRestController {

    @Autowired
    OrdersService ordersService;


    @GetMapping("/fetch-ordeHeader-by-resellerID")
    public Object fetchOrderHeaderListByResellerID(@RequestParam("resellerID") String resellerID ,
                                                   @RequestParam("returnOverallCost") boolean returnOverallPrice,
                                                   @RequestParam("isIccidQuery") boolean isIccidQuery,
                                                   @RequestParam("isExcelRequest") boolean isExcelRequest){
        return ordersService.fetchOrderHeaderListByResellerID(resellerID,returnOverallPrice,isIccidQuery,isExcelRequest);
    }
}
