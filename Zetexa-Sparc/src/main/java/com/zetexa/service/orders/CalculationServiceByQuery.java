package com.zetexa.service.orders;

import com.zetexa.Pojo.UsageCostResponse;
import com.zetexa.entity.Orders.ESimUsageHistory;
import com.zetexa.entity.Reseller.ResellerRoamingRates;
import com.zetexa.entity.Reseller.ZetexaRoamingRates;
import com.zetexa.repository.reseller.ResellerRoamingRatesRepository;
import com.zetexa.repository.reseller.ZetexaRoamingRatesRepository;
import com.zetexa.service.ExcelService.ExcelService;
import com.zetexa.service.sftp.ESimUsageWithICCIDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@SuppressWarnings("unchecked")
@Service
public class CalculationServiceByQuery {

    @Autowired
    ESimUsageWithICCIDService eSimUsageWithICCIDService;

    @Autowired
    ZetexaRoamingRatesRepository zetexaRoamingRatesRepository;

    @Autowired
    ResellerRoamingRatesRepository resellerRoamingRatesRepository;

    @Autowired
    ExcelService excelService;

    @Value("${usdPrice}")
    private double usgPrice;

    private static final Logger logger = LoggerFactory.getLogger(CalculationServiceByQuery.class);



    public UsageCostResponse calculateUsageCostForZetexaFromSparc(String iccid, ESimUsageHistory usage, boolean isZetexaPriceRequest,String resellerID) {
        logger.info("-----call came to calculate the data usage and cost for Internal Zetexa--------");
        String mccmnc = usage.getMcc() + "" + usage.getMnc();

        List<ZetexaRoamingRates> rates = zetexaRoamingRatesRepository.findByMccAndMnc(mccmnc);
        if (rates.isEmpty()) return null;

        ZetexaRoamingRates rate = rates.getFirst();
        double eurRatePerMb = Double.parseDouble(rate.getData());

        double eurToUsdRate = usgPrice;
        double usdRatePerMb = eurRatePerMb * eurToUsdRate;

        double quantityUsedMb = convertBytesToMb(usage.getTotalQty());
        double roundedMb = new BigDecimal(quantityUsedMb).setScale(3, RoundingMode.HALF_UP).doubleValue();


        double totalPrice = usdRatePerMb * quantityUsedMb ;
        return new UsageCostResponse(iccid, mccmnc, totalPrice, rate.getCountryName()
                ,rate.getOperatorName(),"Zetexa",resellerID,roundedMb+"  (MB) ",usage.getPrepaidPackageID());
    }
    public UsageCostResponse calculateUsageCostForReseller(String iccid, ESimUsageHistory usage,boolean isZetexaPriceRequest,String resellerID) {
        logger.info("-----call came to calculate the data usage and cost for Reseller--------");
        String mccmnc = usage.getMcc() + "" + usage.getMnc();

        List<ResellerRoamingRates> resellerRoamingRates = resellerRoamingRatesRepository.findByMccAndMnc(mccmnc);
        if (resellerRoamingRates.isEmpty()) return null;

        ResellerRoamingRates rate = resellerRoamingRates.getFirst();
        double eurRatePerMb = Double.parseDouble(rate.getData());

        double eurToUsdRate = usgPrice;
        double usdRatePerMb = eurRatePerMb * eurToUsdRate;

        double quantityUsedGb = convertBytesToGb(usage.getTotalQty());

        double totalPrice =eurRatePerMb* quantityUsedGb ;

        return new UsageCostResponse(iccid, mccmnc, totalPrice, rate.getCountryName(),rate.getOperatorName(),"Zetexa",resellerID,String.valueOf(quantityUsedGb) +" (GB) ",usage.getPrepaidPackageID());
    }
    private double convertBytesToMb(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }
    private double convertBytesToGb(long bytes) {
        return bytes / (1024.0 * 1024.0 * 1024.0);
    }


    public Object prepareResponseForEacICCID(List<UsageCostResponse> usageCostResponses,boolean isExcelRequest){
        logger.info("------Call came to Prepare Excel Sheet For ICCID base Response--------------");
        return  isExcelRequest ? excelService.prapreExcelForUsageResponseCost(usageCostResponses) : usageCostResponses;
    }


    public Object prepareTotalPriceForICCIDSForReseller(List<String> iccidList,
                                                         List<UsageCostResponse> responseList,
                                                         String resellerID,
                                                         boolean isExcelRequest) {
        double totalCost = responseList.stream().mapToDouble(UsageCostResponse::getTotalCost).sum();
        Map<String,Object> resellerResponseMap = new HashMap<>();
        resellerResponseMap.put("ICCID's",iccidList);
        resellerResponseMap.put("ResellerID",resellerID);
        resellerResponseMap.put("Total Cost",totalCost);

        return isExcelRequest ?
                excelService.prepareTotalPriceExcelResponse(iccidList,responseList,resellerID)
                : resellerResponseMap;
    }
    public Object buildIccidSummary(List<UsageCostResponse> responseList,boolean isExcelRequest,String resellerID) {
        logger.info("-----call came to prepare the total cost by iccid---------------");

        Map<String, Map<String, Object>> iccidSummaryMap = new LinkedHashMap<>();

        for (UsageCostResponse response : responseList) {
            String iccid = response.getIccid();
            iccidSummaryMap.putIfAbsent(iccid, new LinkedHashMap<>());

            Map<String, Object> innerMap = iccidSummaryMap.get(iccid);
            innerMap.put("ICCID", iccid);

            Object totalCostObj = innerMap.get("Total Cost");
            double currentTotal = (totalCostObj instanceof Number) ? ((Number) totalCostObj).doubleValue() : 0.0;
            double formattedTotalCost = Math.round((currentTotal + response.getTotalCost()) * 10000.0) / 10000.0;
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
        return  isExcelRequest ?
                excelService.exportIccidSummaryToExcelResponse(finalList,"Reseller_Summary_" + resellerID + ".xlsx")
                : finalList;
    }
}
