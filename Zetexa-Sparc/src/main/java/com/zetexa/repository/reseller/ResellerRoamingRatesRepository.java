package com.zetexa.repository.reseller;

import com.zetexa.entity.Reseller.ResellerRoamingRates;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResellerRoamingRatesRepository extends JpaRepository<ResellerRoamingRates, Long> {

    @Query(value = "SELECT * FROM reseller_roaming_rates WHERE mccmnc = :mccmnc", nativeQuery = true)
    List<ResellerRoamingRates> findByMccAndMnc(String mccmnc);
}
