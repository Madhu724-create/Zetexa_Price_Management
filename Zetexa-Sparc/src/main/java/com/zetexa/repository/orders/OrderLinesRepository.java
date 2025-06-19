package com.zetexa.repository.orders;

import com.zetexa.entity.Orders.OrderLines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderLinesRepository extends JpaRepository<OrderLines,Long> {
}
