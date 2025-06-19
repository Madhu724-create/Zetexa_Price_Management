package com.zetexa.controller.sftp;


import com.zetexa.entity.Orders.ESimUsageHistory;
import com.zetexa.entity.Orders.SftpFileTrack;
import com.zetexa.repository.orders.ESimUsageRepository;
import com.zetexa.service.sftp.SftpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sftp")
public class SFTPController {

    @Autowired
    private SftpService sftpService;

    @Autowired
    ESimUsageRepository eSimUsageRepository;

    @GetMapping("/fetchCsvData")
    public String fetchCsvData() {
      return  sftpService.fetchAndPersistCsvDataFromSftp();
    }

    @GetMapping("/findAllSftp")
    public List<ESimUsageHistory> findAll(){
        return eSimUsageRepository.findAll();
    }
}
