package com.zetexa.entity.Orders;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ordersapp_orderlines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLines {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderHeader order;

    @Column(name = "package_id", nullable = false, length = 100)
    private String packageId;

    @Column(name = "package_name", nullable = false, length = 386)
    private String packageName;

    @Column(name = "package_template_id", nullable = false, length = 386)
    private String packageTemplateId;

    @Column(name = "package_data", length = 256)
    private String packageData;

    @Column(name = "package_call")
    private Integer packageCall;

    @Column(name = "package_sms")
    private Integer packageSms;

    @Column(name = "period_days")
    private Long periodDays;

    @Column(name = "package_coverage", nullable = false, length = 5)
    private String packageCoverage = "4G";

    @Column(name = "package_locations")
    private String packageLocations;

    @Column(name = "is_kyc_mandatory")
    private boolean isKycMandatory = false;

    @Column(name = "is_kyc_completed")
    private Boolean isKycCompleted = false;

    @Column(name = "is_qr_code_mail_snt")
    private Boolean isQrCodeMailSnt = false;

    @Column(name = "is_shipping_required")
    private boolean isShippingRequired = false;

    @Min(1)
    @Column(name = "quantity")
    private Integer quantity;

    @Min(0)
    @Column(name = "quantity_fulfilled")
    private Integer quantityFulfilled = 0;

    @Min(0)
    @Column(name = "quantity_invoiced")
    private Integer quantityInvoiced = 0;

    @Column(name = "quantity_cancelled")
    private Integer quantityCancelled = 0;

    @Column(name = "currency", length = 5)
    private String currency;

    @Column(name = "currency_rate", precision = 10, scale = 2)
    private BigDecimal currencyRate = BigDecimal.valueOf(1.0);

    @Column(name = "unit_price_net_amount", precision = 10, scale = 2)
    private BigDecimal unitPriceNetAmount;

    @Column(name = "unit_price_gross_amount", precision = 10, scale = 2)
    private BigDecimal unitPriceGrossAmount;

    @Column(name = "total_price_net_amount", precision = 10, scale = 2)
    private BigDecimal totalPriceNetAmount;

    @Column(name = "total_price_gross_amount", precision = 10, scale = 2)
    private BigDecimal totalPriceGrossAmount;

    @Column(name = "base_unit_price_amount", precision = 10, scale = 2)
    private BigDecimal baseUnitPriceAmount = BigDecimal.ZERO;

    @Column(name = "tax_rate", precision = 5, scale = 4)
    private BigDecimal taxRate;

    @Column(name = "reseller_id")
    private UUID resellerId;

    @Column(name = "agent_id")
    private UUID agentId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_details")
    private String customerDetails;

    @Column(name = "customer_nationality", length = 256)
    private String customerNationality;

    @Column(name = "destination", length = 256)
    private String destination;

    @Column(name = "markup_percentage")
    private Float markupPercentage = 0.0f;

    @Column(name = "physical_sim_cost", precision = 10, scale = 4)
    private BigDecimal physicalSimCost;

    @Column(name = "physical_sim_cost_currency", length = 10)
    private String physicalSimCostCurrency;

    @Column(name = "plan_type", length = 25)
    private String planType;

    @Column(name = "sim_type", length = 25)
    private String simType;

    @Column(name = "iccid", length = 100)
    private String iccid;

    @Column(name = "plan_activation_date")
    private LocalDateTime planActivationDate;

    @Column(name = "order_type", length = 50)
    private String orderType = "NEW_ESIM";

    @Column(name = "is_refunded")
    private Boolean isRefunded = false;

    @Column(name = "refund_comment", length = 100)
    private String refundComment;

    @Column(name = "refunded_amount")
    private Float refundedAmount = 0.0f;

    @Column(name = "refund_details")
    private String refundDetails;

    @Column(name = "preferred_vendor", length = 100)
    private String preferredVendor = "spark";

    @PrePersist
    public void prePersist() {
        if (quantityFulfilled == null) quantityFulfilled = 0;
        if (quantityInvoiced == null) quantityInvoiced = 0;
        if (quantityCancelled == null) quantityCancelled = 0;
    }
}
