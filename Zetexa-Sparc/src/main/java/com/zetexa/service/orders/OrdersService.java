package com.zetexa.service.orders;

import com.zetexa.Pojo.DatesUtility;
import com.zetexa.entity.Orders.OrderFulfillment;
import com.zetexa.entity.Orders.OrderHeader;
import com.zetexa.repository.orders.OrderFulfillmentRepository;
import com.zetexa.repository.orders.OrdersHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
                                                   boolean isExcelRequest,
                                                   boolean isZetexaPriceRequest,
                                                   Map<String,String> datesCriteria) {

        Date fromDate = DatesUtility.parseDates(datesCriteria.get("fromDate"));
        Date toDate = DatesUtility.parseDates(datesCriteria.get("toDate"));

        List<OrderHeader> orderHeaderList = ordersHeaderRepository.findByResellerIDAndDateRange(resellerID, fromDate,toDate);
        UUID targetId = UUID.fromString("8bcb9b2f-4aa8-4d4a-b46b-d41f1b307363");
        List<OrderHeader> filteredList = orderHeaderList.stream()
                .filter(orderHeader -> orderHeader.getId().equals(targetId))
                .toList();

        return fetchOrdersFulfilmentWithOrderID(filteredList,returnOverallPrice,resellerID,isIccidQuery,isExcelRequest,isZetexaPriceRequest,fromDate,toDate);
    }


    private Object fetchOrdersFulfilmentWithOrderID(List<OrderHeader> orderHeaderList ,
                                                    boolean returnOverallPrice,
                                                    String resellerID,
                                                    boolean isIccidQuery,
                                                    boolean isExcelRequest ,
                                                    boolean isZetexaPriceRequest,
                                                    Date fromDate ,
                                                    Date toDate) {
        List<OrderFulfillment> totalOrderFulfillmentList = new ArrayList<>();
        for (OrderHeader orderHeader : orderHeaderList) {
            List<OrderFulfillment> orderFulfillmentList = orderFulfillmentRepository.findByOrderID(orderHeader.getId());
            List<OrderFulfillment> filteredList = orderFulfillmentList.stream()
                    .filter(f -> f.getCreatedOn() != null &&
                            !f.getCreatedOn().before(fromDate) &&
                            !f.getCreatedOn().after(toDate))
                    .toList();

            totalOrderFulfillmentList.addAll(filteredList);
        }
       return pricingCalculationService.processFulfillmentAndCalculatePricing(totalOrderFulfillmentList,returnOverallPrice,resellerID,isIccidQuery,isExcelRequest,isZetexaPriceRequest);
    }

}
