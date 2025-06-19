package com.zetexa.roamingRates;

import com.zetexa.entity.Reseller.ResellerRoamingRates;
import com.zetexa.entity.Reseller.ZetexaRoamingRates;
import com.zetexa.repository.reseller.ResellerRoamingRatesRepository;
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
public class ExtractRateExcelForResellerPricingService {
    
    @Autowired
    ResellerRoamingRatesRepository resellerRoamingRatesRepository;

    private static final Logger logger = LoggerFactory.getLogger(ExtractRateExcelForZetexaPricingService.class);

    public String importFromExcel(MultipartFile file) {
        int records = 0;
        logger.info("------call came to extract the roaming rates and save in db-------------");
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 5; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                ResellerRoamingRates resellerRoamingRates = new ResellerRoamingRates();
                resellerRoamingRates.setCountryName(getString(row, 0));
                resellerRoamingRates.setOperatorName(getString(row, 1));
                resellerRoamingRates.setTadig(getString(row, 2));
                resellerRoamingRates.setMccmnc(getMCCMNCValue(row));
                resellerRoamingRates.setMoc(getString(row, 4));
                resellerRoamingRates.setMtc(getBigDecimal(row, 5));
                resellerRoamingRates.setSms(getString(row, 6));
                resellerRoamingRates.setData(getString(row, 7));
                resellerRoamingRates.setCamelSupport(getString(row, 8));
                resellerRoamingRates.setLteSupport(getString(row, 9));
                resellerRoamingRates.setNsa5gSupport(getString(row, 10));
                resellerRoamingRates.setVoiceChargingInterval(getInteger(row, 11));
                resellerRoamingRates.setDataChargingInterval(getInteger(row, 12));
                resellerRoamingRates.setCurrency("EUR");
                resellerRoamingRatesRepository.save(resellerRoamingRates);
                records++;
            }
            logger.info("----------Rates Sheet was successfully in database  and Total Records inserted  :  -------------" +records);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return records+  "   inserted successfully!";
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
