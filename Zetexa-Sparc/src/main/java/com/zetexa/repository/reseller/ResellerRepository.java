package com.zetexa.repository.reseller;

import com.zetexa.entity.Reseller.Reseller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ResellerRepository extends JpaRepository<Reseller,Long> {


//    @Query(name = "select reseller_id from customers_reseller")
//    List<?> findAllResellerIDList();
}
