package com.zetexa.service.orders;

import com.zetexa.entity.Orders.OrderFulfillment;
import com.zetexa.entity.Orders.OrderHeader;
import com.zetexa.repository.orders.OrderFulfillmentRepository;
import com.zetexa.repository.orders.OrdersHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrdersService {

    @Autowired
    OrdersHeaderRepository ordersHeaderRepository;

    @Autowired
    OrderFulfillmentRepository orderFulfillmentRepository;

    @Autowired
    PricingCalculationService pricingCalculationService;

    public Object fetchOrderHeaderListByResellerID(String resellerID ,
                                                   boolean returnOverallPrice,
                                                   boolean isIccidQuery,
                                                   boolean isExcelRequest) {
        List<OrderHeader> orderHeaderList = ordersHeaderRepository.findByResellerID(resellerID);
        UUID targetId = UUID.fromString("b0c967aa-9ba7-4c4b-a408-98d370ddb1a9");  // todo remove this
        List<OrderHeader> filteredList = orderHeaderList.stream()
                .filter(orderHeader -> orderHeader.getId().equals(targetId))
                .toList();

        return fetchOrdersFulfilmentWithOrderID(filteredList,returnOverallPrice,resellerID,isIccidQuery,isExcelRequest);
    }


    private Object fetchOrdersFulfilmentWithOrderID(List<OrderHeader> orderHeaderList , boolean returnOverallPrice,String resellerID, boolean isIccidQuery,boolean isExcelRequest) {
        List<OrderFulfillment> totalOrderFulfillmentList = new ArrayList<>();
        for (OrderHeader orderHeader : orderHeaderList){
            List<OrderFulfillment> orderFulfillmentList = orderFulfillmentRepository.findByOrderID(orderHeader.getId()); // todo remove iccid
            totalOrderFulfillmentList.addAll(orderFulfillmentList);
        }
       return pricingCalculationService.processFulfillmentAndCalculatePricing(totalOrderFulfillmentList,returnOverallPrice,resellerID,isIccidQuery,isExcelRequest);
    }
}
