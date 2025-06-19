package com.zetexa.roamingRates;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/excel")
public class ExtractRateExcelRestController {

    @Autowired
    ExtractRateExcelForZetexaPricingService extractRateExcelService;

    @Autowired
    ExtractRateExcelForResellerPricingService extractRateExcelForResellerPricingService;

    @GetMapping("/extract-roaming-rates-zetexa")
    public String extractRoamingRatesAndSave(@RequestParam("file") MultipartFile file){
     return extractRateExcelService.importFromExcel(file);
    }
    @GetMapping("/extract-roaming-rates-reseller")
    public String extractRoamingRatesForResellerAndSave(@RequestParam("file") MultipartFile file){
        return extractRateExcelForResellerPricingService.importFromExcel(file);
    }
}
