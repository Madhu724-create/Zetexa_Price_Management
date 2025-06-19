package com.zetexa.repository.orders;

import com.zetexa.entity.Orders.SftpFileTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SftpFileTrackListRepository extends JpaRepository<SftpFileTrack,Long> {

    @Query(value = "select file_name from sftp_file_track",nativeQuery = true)
    public List<String> findAllFileNamesFromSFTPTrack();

    @Query(value = "SELECT * FROM sftp_file_track WHERE file_name = :file_name", nativeQuery = true)
    List<SftpFileTrack> findByFileName(@Param("file_name") String file_name);

}
