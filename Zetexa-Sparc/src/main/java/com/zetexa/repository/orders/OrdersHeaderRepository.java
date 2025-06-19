package com.zetexa.repository.orders;


import com.zetexa.entity.Orders.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersHeaderRepository extends JpaRepository<OrderHeader,Long> {

    @Query("SELECT o FROM OrderHeader o WHERE o.resellerId = :resellerID")
    List<OrderHeader> findByResellerID(@Param("resellerID") String resellerID);

}
