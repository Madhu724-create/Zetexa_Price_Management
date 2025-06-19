package com.zetexa.entity.Orders;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ordersapp_orderfulfillment")
public class OrderFulfillment {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id_id", nullable = false)
    private OrderHeader order;

    @ManyToOne
    @JoinColumn(name = "orderpackage_id_id", nullable = false)
    private OrderLines orderPackage;

    @Column(name = "iccid", length = 450, nullable = false)
    private String iccid;

    @Column(name = "subscriber_package_id", length = 450)
    private String subscriberPackageId;

    @Column(name = "msisdn", length = 450)
    private String msisdn;

    @Column(name = "imsi_number", length = 450, nullable = false)
    private String imsiNumber;

    @Column(name = "status", length = 32)
    private String status = "FULFILLED";

    @Column(name = "old_status", length = 32)
    private String oldStatus = "FULFILLED";

    @Column(name = "lpa_server", length = 450)
    private String lpaServer;

    @Column(name = "qr_code_url", length = 500)
    private String qrCodeUrl;

    @Column(name = "smdp_address", length = 450)
    private String smdpAddress;

    @Column(name = "matching_id", length = 450)
    private String matchingID;

    @Column(name = "puk_code")
    private Integer pukCode;

    @Column(name = "pin")
    private Integer pin;

    @Column(name = "profilestatus", length = 20, nullable = false)
    private String profileStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name = "vendor_id", columnDefinition = "UUID")
    private UUID vendorId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "plan_start_date")
    private Date planStartDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "plan_end_date")
    private Date planEndDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "plan_actived_date_time")
    private Date planActivedDateTime;

    @Column(name = "voice_and_sms")
    private Boolean voiceAndSms;

    @Column(name = "add_services")
    private Boolean addServices;

    @Column(name = "real_phone_number", length = 30)
    private String realPhoneNumber;

    @Column(name = "deassign")
    private Boolean deassign;

    @Column(name = "remove_services")
    private Boolean removeServices;

    @Column(name = "plan_cancel")
    private Boolean planCancel;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "msisdn_expiry_date")
    private Date msisdnExpiryDate;

    @Column(name = "assign")
    private Boolean assign;

    @Column(name = "order_fullfillment_id", columnDefinition = "UUID")
    private UUID orderFullfillmentId;
}
