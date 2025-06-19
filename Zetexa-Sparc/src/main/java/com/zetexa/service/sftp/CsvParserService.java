package com.zetexa.service.sftp;

import com.zetexa.entity.Orders.ESimUsageHistory;
import com.zetexa.repository.orders.ESimUsageRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvParserService {

    @Autowired
    ESimUsageRepository eSimUsageHistoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(CsvParserService.class);

    public void parseAndSaveCsv(InputStream inputStream,String fileName) throws IOException {
        List<ESimUsageHistory> records = new ArrayList<>();
        logger.info("----------Call came to Save the SFTP CSV File info into database-------------------");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {
            for (CSVRecord csv : csvParser) {
                ESimUsageHistory record = new ESimUsageHistory();
                record.setUsageDateUtc(LocalDateTime.parse(csv.get("USAGE_DATE_UTC")));
                record.setSessionId(csv.get("SESSION_ID"));
                record.setMcc(parseInt(csv.get("MCC")));
                record.setMnc(parseInt(csv.get("MNC")));
                record.setTotalQty(parseLong(csv.get("TOTAL_QTY")));
                record.setUsageTypeId(parseInt(csv.get("USAGE_TYPE_ID")));
                record.setUsageType(csv.get("USAGE_TYPE"));
                record.setDestPhoneNumber(csv.get("DEST_PHONE_NUMBER"));
                record.setSubscriberId(parseLong(csv.get("SUBSCRIBER_ID")));
                record.setImsi(csv.get("IMSI"));
                record.setIccid(csv.get("ICCID"));
                record.setSubsPhoneNumber(csv.get("SUBS_PHONE_NUMBER"));
                record.setPrepaidPackageQtys(parseLong(csv.get("PREPAID_PACKAGE_QTYS")));
                record.setCustoCharge(parseDouble(csv.get("CUSTO_CHARGE")));
                record.setApn(csv.get("APN"));
                record.setImei(csv.get("IMEI"));
                record.setDownBitrate(parseLong(csv.get("DOWN_BITRATE")));
                record.setUpBitrate(parseLong(csv.get("UP_BITRATE")));

                records.add(record);
            }
            eSimUsageHistoryRepository.saveAll(records);
            logger.info("------------Csv file data saved successfully---------------------------");
        }
    }

    private Integer parseInt(String value) {
        try { return value == null || value.isBlank() ? null : Integer.parseInt(value); } catch (Exception e) { return null; }
    }
    private Long parseLong(String value) {
        try { return value == null || value.isBlank() ? null : new BigDecimal(value).longValue(); } catch (Exception e) { return null; }
    }
    private Double parseDouble(String value) {
        try { return value == null || value.isBlank() ? null : Double.parseDouble(value); } catch (Exception e) { return null; }
    }
}
