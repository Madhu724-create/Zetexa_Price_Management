package com.zetexa.roamingRates;


import com.zetexa.entity.Reseller.ZetexaRoamingRates;
import com.zetexa.repository.reseller.ZetexaRoamingRatesRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

@Service
public class ExtractRateExcelForZetexaPricingService {

    @Autowired
    ZetexaRoamingRatesRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(ExtractRateExcelForZetexaPricingService.class);

    public String importFromExcel(MultipartFile file) {
        removeRecordsFromExistingRates();
        int records = 0;
        logger.info("------call came to extract the roaming rates and save in db-------------");
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 5; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                ZetexaRoamingRates rate = new ZetexaRoamingRates();
                rate.setCountryName(getString(row, 0));
                rate.setOperatorName(getString(row, 1));
                rate.setTadig(getString(row, 2));
                rate.setMccmnc(getMCCMNCValue(row));
                rate.setMoc(getString(row, 4));
                rate.setMtc(getBigDecimal(row, 5));
                rate.setSms(getString(row, 6));
                rate.setData(getString(row, 7));
                rate.setCamelSupport(getString(row, 8));
                rate.setLteSupport(getString(row, 9));
                rate.setNsa5gSupport(getString(row, 10));
                rate.setVoiceChargingInterval(getInteger(row, 11));
                rate.setDataChargingInterval(getInteger(row, 12));
                rate.setCurrency("EUR");
                repository.save(rate);
                records++;
            }
            logger.info("----------Rates Sheet was successfully in database  and Total Records inserted  :  -------------" +records);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return records+  "   inserted successfully!";
    }

    private void removeRecordsFromExistingRates() {
        logger.info("-------Started removing records from Zetexa_Pricing Rates---------");
        repository.deleteAll();
    }

    private String getMCCMNCValue(Row row) {
        Cell cell = row.getCell(3);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }
    private String getString(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }


    private BigDecimal getBigDecimal(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cell.getNumericCellValue());
            } else if (cell.getCellType() == CellType.STRING) {
                String text = cell.getStringCellValue().replace("€", "").trim();
                return text.equals("-") ? null : new BigDecimal(text);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getInteger(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        return (int) cell.getNumericCellValue();
    }
}
