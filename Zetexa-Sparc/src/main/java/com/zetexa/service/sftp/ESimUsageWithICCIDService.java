package com.zetexa.service.sftp;

import com.zetexa.entity.Orders.ESimUsageHistory;
import com.zetexa.repository.orders.ESimUsageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ESimUsageWithICCIDService {

    @Autowired
    ESimUsageRepository eSimUsageRepository;

    private static final Logger logger = LoggerFactory.getLogger(ESimUsageWithICCIDService.class);

    public Map<String, List<ESimUsageHistory>> getSFTPDetailsWithGroupByICCID(int pageNumber, int pageSize) {
        logger.info("----call came to fetch eSim usage details with pagination----------  PageNumber     :{}  and   PageSize   :{}", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ESimUsageHistory> pagedList = eSimUsageRepository.findAll(pageable);

        return pagedList.stream()
                .filter(record -> record.getIccid() != null)
                .collect(Collectors.groupingBy(
                        ESimUsageHistory::getIccid,
                        Collectors.collectingAndThen(Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(ESimUsageHistory::getUsageDateUtc))
                                        .collect(Collectors.toList()))));
    }
    public List<ESimUsageHistory> findESimUsageHistoryByICCID(String  iccid){
        return eSimUsageRepository.findByICCID(iccid);
    }
}
