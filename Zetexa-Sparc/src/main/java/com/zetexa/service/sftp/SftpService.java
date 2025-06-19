package com.zetexa.service.sftp;

import com.zetexa.config.SftpConfiguration;
import com.zetexa.entity.Orders.SftpFileTrack;
import com.zetexa.repository.orders.SftpFileTrackListRepository;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

@SuppressWarnings("unchecked")
@Service
public class SftpService {

    @Autowired
    SftpConfiguration sftpConfiguration;

    @Autowired
    CsvParserService csvParserService;

    private final SftpFileTrackListRepository sftpFileTrackListRepository;

    private static final Logger logger = LoggerFactory.getLogger(SftpService.class);

    public SftpService(SftpFileTrackListRepository sftpFileTrackListRepository) {
        this.sftpFileTrackListRepository = sftpFileTrackListRepository;
    }
    public String fetchAndPersistCsvDataFromSftp() {
        logger.info("----------Call came to Connect SFTP Connection--------------------");
        int maxRetries = 5;
        int attempt = 0;
        boolean success = false;

        while (attempt < maxRetries && !success) {
            Session session = null;
            ChannelSftp sftpChannel = null;

            try {
                attempt++;
                JSch jsch = new JSch();
                session = jsch.getSession(sftpConfiguration.getUsername(), sftpConfiguration.getHost(), sftpConfiguration.getPort());
                session.setPassword(sftpConfiguration.getPassword());
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect(10000);

                Channel channel = session.openChannel("sftp");
                channel.connect(10000);
                sftpChannel = (ChannelSftp) channel;
                sftpChannel.cd(sftpConfiguration.getRemoteDir());

                Vector<ChannelSftp.LsEntry> files = sftpChannel.ls("*.csv");
                for (ChannelSftp.LsEntry entry : files) {
                    String fileName = entry.getFilename();
                    List<SftpFileTrack> sftpFileTracks = sftpFileTrackListRepository.findByFileName(fileName);
                    if (sftpFileTracks== null || sftpFileTracks.isEmpty()) {
                        try (InputStream stream = sftpChannel.get(fileName)) {
                            csvParserService.parseAndSaveCsv(stream,fileName);
                            saveSftp(fileName,"Processed");
                        }
                    }else{
                        saveSftp(fileName,"File aleady Exsisted");
                        logger.info("--------File Already Existed--------------"+fileName);
                    }
                }
               success = true;
            } catch (Exception e) {
                logger.info("Attempt    " + attempt + "   failed: " + e.getMessage());
                if (attempt == maxRetries) {
                    throw new RuntimeException("SFTP/CSV Processing Failed after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                if (sftpChannel != null) sftpChannel.disconnect();
                if (session != null) session.disconnect();
            }
        }
        return "Successfully inserted the SFTP info into db";
    }
    private void saveSftp(String fileName,String status) {
        sftpFileTrackListRepository.save(new SftpFileTrack(fileName,status));
    }
}


