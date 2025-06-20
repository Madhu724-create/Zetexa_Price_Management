package com.zetexa.service.orders;

import com.zetexa.Pojo.UsageCostResponse;
import com.zetexa.entity.Orders.ESimUsageHistory;
import com.zetexa.entity.Orders.OrderFulfillment;
import com.zetexa.entity.Reseller.ResellerRoamingRates;
import com.zetexa.entity.Reseller.ZetexaRoamingRates;
import com.zetexa.repository.reseller.ResellerRoamingRatesRepository;
import com.zetexa.repository.reseller.ZetexaRoamingRatesRepository;
import com.zetexa.service.ExcelService.ExcelService;
import com.zetexa.service.sftp.CsvParserService;
import com.zetexa.service.sftp.ESimUsageWithICCIDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class PricingCalculationService {

    @Autowired
    ESimUsageWithICCIDService eSimUsageWithICCIDService;

    @Autowired
    ExcelService excelService;

    @Autowired
    CalculationServiceByQuery calculationServiceByQuery;

    private static final Logger logger = LoggerFactory.getLogger(PricingCalculationService.class);

    public Object processFulfillmentAndCalculatePricing( List<OrderFulfillment> totalOrderFulfillmentList ,
                                                         boolean returnOverallPrice,
                                                         String resellerID,
                                                         boolean isIccidQuery,
                                                         boolean isExcelRequest,
                                                         boolean isZetexaPriceRequest) {
     logger.info("-----call came to calculate the rates ----------------");
     List<ESimUsageHistory> finalEsESimUsageHistoryList = new ArrayList<>();
        Set<String> iccidSet = new HashSet<>(totalOrderFulfillmentList.stream().map(OrderFulfillment::getIccid).collect(Collectors.toSet()));

        List<String> iccidList = new ArrayList<>(iccidSet);
     for (String eachICCID : iccidList){
         finalEsESimUsageHistoryList.addAll(eSimUsageWithICCIDService.findESimUsageHistoryByICCID(eachICCID));
     }
     if(!finalEsESimUsageHistoryList.isEmpty()){
      return findAllZetexaRatesByMccAndMnc(finalEsESimUsageHistoryList,iccidList,returnOverallPrice ,resellerID,isIccidQuery,isExcelRequest,isZetexaPriceRequest);
     }
    return null;
    }

    private Object findAllZetexaRatesByMccAndMnc(List<ESimUsageHistory> finalEsESimUsageHistoryList ,
                                                 List<String> iccidList ,
                                                 boolean returnOverallPrice ,
                                                 String resellerID,
                                                 boolean isIccidQuery,
                                                 boolean isExcelRequest,
                                                 boolean isZetexaPriceRequest) {

        logger.info("---------- Fetching Zetexa Rates by MCC+MNC from CSV data --------------"+"\n" +finalEsESimUsageHistoryList);
        Map<String, List<ESimUsageHistory>> groupedByIccid = groupByIccid(finalEsESimUsageHistoryList);
        List<UsageCostResponse> responseList = new ArrayList<>();

        for (Map.Entry<String, List<ESimUsageHistory>> entry : groupedByIccid.entrySet()) {
            String iccid = entry.getKey();
            List<ESimUsageHistory> usageList = entry.getValue();

            for (ESimUsageHistory usage : usageList) {
                UsageCostResponse response = isZetexaPriceRequest ?
                        calculationServiceByQuery.calculateUsageCostForZetexaFromSparc(iccid, usage,isZetexaPriceRequest,resellerID) :
                        calculationServiceByQuery.calculateUsageCostForReseller(iccid,usage,isZetexaPriceRequest,resellerID);

                if (response != null) {
                    responseList.add(response);
                }
            }
        }
        if(isIccidQuery){
          return  calculationServiceByQuery.buildIccidSummary(responseList,isExcelRequest,resellerID);
        }
        return returnOverallPrice ? calculationServiceByQuery.prepareTotalPriceForICCIDSForReseller(iccidList,responseList,resellerID,isExcelRequest) : calculationServiceByQuery.prepareResponseForEacICCID(responseList,isExcelRequest);
    }

    private Map<String, List<ESimUsageHistory>> groupByIccid(List<ESimUsageHistory> list) {
        return list.stream().
                filter(e -> e.getIccid() != null && e.getMcc() != null && e.getMnc() != null)
                .collect(Collectors.groupingBy(ESimUsageHistory::getIccid));
    }
}
