package com.zetexa.repository.orders;


import com.zetexa.entity.Orders.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrdersHeaderRepository extends JpaRepository<OrderHeader,Long> {

    @Query("SELECT o FROM OrderHeader o WHERE o.resellerId = :resellerID AND o.createdOn BETWEEN :startDate AND :endDate")
    List<OrderHeader> findByResellerIDAndDateRange(
            @Param("resellerID") String resellerID,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

}
