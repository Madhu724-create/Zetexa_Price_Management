package com.zetexa.repository.orders;

import com.zetexa.entity.Orders.OrderFulfillment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Repository
public interface OrderFulfillmentRepository  extends JpaRepository<OrderFulfillment,Long> {

    @Query("SELECT o FROM OrderFulfillment o WHERE o.order.id = :orderId")
    List<OrderFulfillment> findByOrderID(@Param("orderId") UUID orderId );

/*
    @Query("SELECT o FROM OrderFulfillment o WHERE o.order.id = :orderId AND o.createdOn BETWEEN :fromDate AND :toDate")
    List<OrderFulfillment> findByOrderID(@Param("orderId") UUID orderId , @Param("fromDate")Date fromDate , @Param("toDate") Date toDate);*/

}
