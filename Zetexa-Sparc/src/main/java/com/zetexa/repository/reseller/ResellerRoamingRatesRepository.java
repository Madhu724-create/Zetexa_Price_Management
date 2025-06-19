package com.zetexa.repository.reseller;

import com.zetexa.entity.Reseller.ResellerRoamingRates;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResellerRoamingRatesRepository extends JpaRepository<ResellerRoamingRates, Long> {
}
