package com.zetexa.entity.Orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sftp_file_track")
public class SftpFileTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "file_name", nullable = false, unique = true)
    private String fileName;

    private String status;

    @Column(name = "inserted_date", nullable = false)
    private LocalDateTime insertedDate = LocalDateTime.now();

    public SftpFileTrack() {}

    public SftpFileTrack(String fileName,String status) {
        this.fileName = fileName;
        this.status = status;
    }

}
