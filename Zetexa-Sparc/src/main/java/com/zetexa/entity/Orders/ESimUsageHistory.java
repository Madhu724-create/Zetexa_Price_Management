package com.zetexa.entity.Orders;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sftp_esim_usage_cdr")
@ToString
public class ESimUsageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usage_date_utc")
    private LocalDateTime usageDateUtc;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "mcc")
    private Integer mcc;

    @Column(name = "mnc")
    private Integer mnc;

    @Column(name = "total_qty")
    private Long totalQty;

    @Column(name = "usage_type_id")
    private Integer usageTypeId;

    @Column(name = "usage_type")
    private String usageType;

    @Column(name = "dest_phone_number")
    private String destPhoneNumber;

    @Column(name = "subscriber_id")
    private Long subscriberId;

    @Column(name = "imsi")
    private String imsi;

    @Column(name = "iccid")
    private String iccid;

    @Column(name = "subs_phone_number")
    private String subsPhoneNumber;

    @Column(name = "prepaid_package_qtys")
    private Long prepaidPackageQtys;

    @Column(name = "custo_charge")
    private Double custoCharge;

    @Column(name = "apn")
    private String apn;

    @Column(name = "imei")
    private String imei;

    @Column(name = "down_bitrate")
    private Long downBitrate;

    @Column(name = "up_bitrate")
    private Long upBitrate;

    @Column(name = "file_name")
    private Long fileName;

}