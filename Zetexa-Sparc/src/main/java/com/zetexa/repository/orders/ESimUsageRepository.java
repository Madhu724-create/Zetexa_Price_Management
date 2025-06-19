package com.zetexa.repository.orders;

import com.zetexa.entity.Orders.ESimUsageHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ESimUsageRepository extends JpaRepository<ESimUsageHistory,Long> {

    Page<ESimUsageHistory> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM sftp_esim_usage_cdr WHERE iccid = :iccid", nativeQuery = true)
    List<ESimUsageHistory> findByICCID(@Param("iccid") String iccid);

}
