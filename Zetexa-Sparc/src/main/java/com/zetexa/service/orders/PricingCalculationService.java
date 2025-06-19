package com.zetexa.service.orders;

import com.zetexa.Pojo.UsageCostResponse;
import com.zetexa.entity.Orders.ESimUsageHistory;
import com.zetexa.entity.Orders.OrderFulfillment;
import com.zetexa.entity.Reseller.ZetexaRoamingRates;
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
import java.util.stream.Collectors;


@Service
@SuppressWarnings("unchecked")
public class PricingCalculationService {

    @Autowired
    ESimUsageWithICCIDService eSimUsageWithICCIDService;

    @Autowired
    ZetexaRoamingRatesRepository zetexaRoamingRatesRepository;

    @Autowired
    ExcelService excelService;

    @Value("${usdPrice}")
    private double usgPrice;

    @Value("${isZetexaPriceRequest}")
    private boolean isZetexaPriceRequest;

    private static final Logger logger = LoggerFactory.getLogger(PricingCalculationService.class);

    public Object processFulfillmentAndCalculatePricing( List<OrderFulfillment> totalOrderFulfillmentList , boolean returnOverallPrice,String resellerID, boolean isIccidQuery,boolean isExcelRequest) {
     logger.info("-----call came to calculate the rates ----------------");
     List<ESimUsageHistory> finalEsESimUsageHistoryList = new ArrayList<>();
        Set<String> iccidSet = new HashSet<>(totalOrderFulfillmentList.stream().map(OrderFulfillment::getIccid).collect(Collectors.toSet()));

        List<String> iccidList = new ArrayList<>(iccidSet);
     for (String eachICCID : iccidList){
         List<ESimUsageHistory> eSimUsageHistoryList = eSimUsageWithICCIDService.findESimUsageHistoryByICCID(eachICCID);
         finalEsESimUsageHistoryList.addAll(eSimUsageHistoryList);
     }
     if(!finalEsESimUsageHistoryList.isEmpty()){
      return findAllZetexaRatesByMccAndMnc(finalEsESimUsageHistoryList,iccidList,returnOverallPrice ,resellerID,isIccidQuery,isExcelRequest);
     }
    return null;
    }
    private Object findAllZetexaRatesByMccAndMnc(List<ESimUsageHistory> finalEsESimUsageHistoryList ,List<String> iccidList , boolean returnOverallPrice , String resellerID, boolean isIccidQuery,boolean isExcelRequest) {
        logger.info("---------- Fetching Zetexa Rates by MCC+MNC from CSV data --------------"+"\n" +finalEsESimUsageHistoryList);
        Map<String, List<ESimUsageHistory>> groupedByIccid = groupByIccid(finalEsESimUsageHistoryList);
        List<UsageCostResponse> responseList = new ArrayList<>();

        for (Map.Entry<String, List<ESimUsageHistory>> entry : groupedByIccid.entrySet()) {
            String iccid = entry.getKey();
            List<ESimUsageHistory> usageList = entry.getValue();

            for (ESimUsageHistory usage : usageList) {
                UsageCostResponse response = calculateUsageCost(iccid, usage);
                if (response != null) {
                    responseList.add(response);
                }
            }
        }
        if(isIccidQuery){
          return  buildIccidSummary(responseList,isExcelRequest,resellerID);
        }
        return returnOverallPrice ? prepareTotalPriceForICCIDSForReseller(iccidList,responseList,resellerID,isExcelRequest) : prepareExcelReportForICCIDBase(responseList,isExcelRequest);
    }

    private Object prepareTotalPriceForICCIDSForReseller(List<String> iccidList, List<UsageCostResponse> responseList, String resellerID,boolean isExcelRequest) {
      double totalCost = responseList.stream().mapToDouble(UsageCostResponse::getTotalPrice).sum();
        Map<String,Object>  resellerResponseMap = new HashMap<>();
      resellerResponseMap.put("ICCID's",iccidList);
      resellerResponseMap.put("ResellerID",resellerID);
      resellerResponseMap.put("Total Cost",totalCost);
      return isExcelRequest ? excelService.prepareTotalPriceExcelResponse(iccidList,responseList,resellerID) : resellerResponseMap;
    }

    private Object prepareExcelReportForICCIDBase(List<UsageCostResponse> usageCostResponses,boolean isExcelRequest){
        logger.info("------Call came to Prepare Excel Sheet For ICCID base Response--------------");
        return  isExcelRequest ? excelService.prapreExcelForUsageResponseCost(usageCostResponses) : usageCostResponses;
    }

    private Map<String, List<ESimUsageHistory>> groupByIccid(List<ESimUsageHistory> list) {
        return list.stream().filter(e -> e.getIccid() != null && e.getMcc() != null && e.getMnc() != null)
                .collect(Collectors.groupingBy(ESimUsageHistory::getIccid));
    }

    private UsageCostResponse calculateUsageCost(String iccid, ESimUsageHistory usage) {
        logger.info("-----call came to calculate the data usage and cost--------");
        String mccmnc = usage.getMcc() + "" + usage.getMnc();

        List<ZetexaRoamingRates> rates = zetexaRoamingRatesRepository.findByMccAndMnc(mccmnc);
        if (rates.isEmpty()) return null;

        ZetexaRoamingRates rate = rates.getFirst();
        double eurRatePerMb = Double.parseDouble(rate.getData());

        double eurToUsdRate = usgPrice;
        double usdRatePerMb = eurRatePerMb * eurToUsdRate;

        double quantityUsedMb = convertBytesToMb(usage.getTotalQty());
        double quantityUsedGb = convertBytesToGb(usage.getTotalQty());

        double totalPrice = isZetexaPriceRequest ? usdRatePerMb * quantityUsedMb : eurRatePerMb* quantityUsedGb ; //for zetexa , USD/MB , Reseller EUR/GB

        return new UsageCostResponse(iccid, mccmnc, totalPrice, rate.getCountryName(),rate.getOperatorName());
    }
    private double convertBytesToMb(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }
    private double convertBytesToGb(long bytes) {
        return bytes / (1024.0 * 1024.0 * 1024.0);
    }

    private Object buildIccidSummary(List<UsageCostResponse> responseList,boolean isExcelRequest,String resellerID) {
        logger.info("-----call came to prepare the total cost by iccid---------------");

        Map<String, Map<String, Object>> iccidSummaryMap = new LinkedHashMap<>();

        for (UsageCostResponse response : responseList) {
            String iccid = response.getIccid();
            iccidSummaryMap.putIfAbsent(iccid, new LinkedHashMap<>());

            Map<String, Object> innerMap = iccidSummaryMap.get(iccid);
            innerMap.put("ICCID", iccid);

            Object totalCostObj = innerMap.get("Total Cost");
            double currentTotal = (totalCostObj instanceof Number) ? ((Number) totalCostObj).doubleValue() : 0.0;
            double formattedTotalCost = Math.round((currentTotal + response.getTotalPrice()) * 10000.0) / 10000.0;
            innerMap.put("Total Cost", formattedTotalCost);

            List<String> operators = (List<String>) innerMap.get("Operators");
            if (operators == null) {
                operators = new ArrayList<>();
            }
            if (response.getOperatorName() != null && !operators.contains(response.getOperatorName())) {
                operators.add(response.getOperatorName());
            }
            innerMap.put("Operators", operators);
        }

        List<Map<String, Map<String, Object>>> finalList = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry : iccidSummaryMap.entrySet()) {
            finalList.add(Collections.singletonMap(entry.getKey(), entry.getValue()));
        }

        return  isExcelRequest ? excelService.exportIccidSummaryToExcelResponse(finalList,"Reseller_Summary_" + resellerID + ".xlsx") : finalList;
    }

}
