package com.zetexa.service.ExcelService;

import com.zetexa.Pojo.UsageCostResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    public ResponseEntity<byte[]> prepareTotalPriceExcelResponse(List<String> iccidList, List<UsageCostResponse> responseList, String resellerID) {
        logger.info("------call came to prepare excel for Total Cost calculation for Reseller-------------" + resellerID);
        double totalCost = responseList.stream().mapToDouble(UsageCostResponse::getTotalCost).sum();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Reseller Summary");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ResellerID", "Total Cost", "ICCID"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < iccidList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(resellerID);
                row.createCell(1).setCellValue(totalCost);
                row.createCell(2).setCellValue(iccidList.get(i));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headersResponse.setContentDispositionFormData("attachment", "Reseller_Summary_" + resellerID + ".xlsx");

            return ResponseEntity.ok().headers(headersResponse).body(out.toByteArray());

        } catch (Exception e) {
            logger.error("Error generating Reseller Excel: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<byte[]> exportIccidSummaryToExcelResponse(List<Map<String, Map<String, Object>>> iccidSummaryList, String fileName) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("ICCID Summary");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ICCID", "Total Cost", "Operators"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Map<String, Map<String, Object>> entry : iccidSummaryList) {
                for (Map.Entry<String, Map<String, Object>> iccidEntry : entry.entrySet()) {
                    Map<String, Object> data = iccidEntry.getValue();

                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue((String) data.get("ICCID"));
                    row.createCell(1).setCellValue((double) data.get("Total Cost"));

                    @SuppressWarnings("unchecked")
                    List<String> operators = (List<String>) data.get("Operators");
                    row.createCell(2).setCellValue(String.join(", ", operators));
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            byte[] excelBytes = out.toByteArray();

            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headersResponse.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headersResponse)
                    .body(excelBytes);

        } catch (Exception e) {
            logger.info("------------Exception Occurred while Preparing Excel file for ICCID base-------------" + e);
            return ResponseEntity.internalServerError().build();
        }
    }
    public ResponseEntity<byte[]> prapreExcelForUsageResponseCost(List<UsageCostResponse> usageCostResponses) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Usage Cost Summary");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] headers = {
                    "ICCID", "MCCMNC", "Total Cost", "Country Name", "Operator Name",
                    "Parent Reseller Name", "Reseller Name", "Data", "Package ID"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (UsageCostResponse response : usageCostResponses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(response.getIccid());
                row.createCell(1).setCellValue(response.getMccmnc());
                row.createCell(2).setCellValue(response.getTotalCost());
                row.createCell(3).setCellValue(response.getCountyName());
                row.createCell(4).setCellValue(response.getOperatorName());
                row.createCell(5).setCellValue(response.getParentResellerName());
                row.createCell(6).setCellValue(response.getResellerName());
                row.createCell(7).setCellValue(response.getData());
                row.createCell(8).setCellValue(response.getPackageID());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            byte[] excelBytes = out.toByteArray();

            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headersResponse.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Usage_Cost_Summary.xlsx");

            return ResponseEntity.ok()
                    .headers(headersResponse)
                    .body(excelBytes);

        } catch (Exception e) {
            logger.error("Exception while preparing Excel for Usage Cost Response: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }


}
