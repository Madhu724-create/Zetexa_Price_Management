package com.zetexa.service.sftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
public class SFTPSchedulerService {

    @Autowired
    SftpService sftpService;

    private static final Logger logger = LoggerFactory.getLogger(SFTPSchedulerService.class);


    // @Scheduled(scheduler ="${cronJobForSFTP}" )
    public String integrateWithSFTPAndInsertIntoDatabase(){
     logger.info("--------Scheduler started to fetch Csv data from SFTP--------------------------");
     sftpService.fetchAndPersistCsvDataFromSftp();
     return "Successfully inserted the SFTP CSV file data in DB";
    }
}
