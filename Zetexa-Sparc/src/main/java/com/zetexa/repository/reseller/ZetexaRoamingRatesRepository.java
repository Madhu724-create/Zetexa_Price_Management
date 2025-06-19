package com.zetexa.repository.reseller;


import com.zetexa.entity.Reseller.ZetexaRoamingRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZetexaRoamingRatesRepository extends JpaRepository<ZetexaRoamingRates,Long> {

    @Query(value = "SELECT * FROM zetexa_roaming_rates WHERE mccmnc = :mccmnc", nativeQuery = true)
    List<ZetexaRoamingRates> findByMccAndMnc(@Param("mccmnc") String mccmnc);

}
