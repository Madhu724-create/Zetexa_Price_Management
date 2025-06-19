package com.zetexa.entity.Orders;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "ordersapp_orderheader" , schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHeader {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "customer_email")
    private String customerEmail = "";

    @Column(name = "reference_id", length = 600)
    private String referenceId;

    @Column(name = "checkout_id", columnDefinition = "UUID")
    private UUID checkoutId;

    @Column(name = "billing_firstname", nullable = false)
    private String billingFirstname;

    @Column(name = "billing_lastname", nullable = false)
    private String billingLastname;

    @Column(name = "billing_address", length = 500)
    private String billingAddress;

    @Column(name = "billing_state")
    private String billingState;

    @Column(name = "billing_city")
    private String billingCity;

    @Column(name = "billing_postal_code", length = 256)
    private String billingPostalCode;

    @Column(name = "billing_country", nullable = false)
    private String billingCountry;

    @Column(name = "billing_country_code", length = 2)
    private String billingCountryCode;

    @Column(name = "currency", length = 5)
    private String currency;

    @Column(name = "currency_rate", precision = 10, scale = 2)
    private BigDecimal currencyRate = BigDecimal.valueOf(1.0);

    @Column(name = "payment_method", nullable = false)
    private Integer paymentMethod;

    @Column(name = "payment_method_name", length = 100)
    private String paymentMethodName = "";

    @Column(name = "payment_data", columnDefinition = "jsonb")
    private String paymentData;

    @Column(name = "total_net_amount", precision = 10, scale = 2)
    private BigDecimal totalNetAmount = BigDecimal.ZERO;

    @Column(name = "undiscounted_total_net_amount", precision = 10, scale = 2)
    private BigDecimal undiscountedTotalNetAmount = BigDecimal.ZERO;

    @Column(name = "total_gross_amount", precision = 10, scale = 2)
    private BigDecimal totalGrossAmount = BigDecimal.ZERO;

    @Column(name = "undiscounted_total_gross_amount", precision = 10, scale = 2)
    private BigDecimal undiscountedTotalGrossAmount = BigDecimal.ZERO;

    @Column(name = "total_tax", precision = 10, scale = 2)
    private BigDecimal totalTax = BigDecimal.ZERO;

    @Column(name = "total_sum", precision = 10, scale = 2)
    private BigDecimal totalSum = BigDecimal.ZERO;

    // You can calculate or map `total_net`, `total_gross`, etc. separately in service or using @Transient.

    @Column(name = "coins")
    private Integer coins = 0;

    @Column(name = "customer_note", columnDefinition = "text")
    private String customerNote = "";

    @Column(name = "coins_spent_amount")
    private Double coinsSpentAmount = 0.0;

    @Column(name = "coupon_code", length = 255)
    private String couponCode;

    @Column(name = "coupon_value")
    private Double couponValue = 0.0;

    @Column(name = "customer_ip_address")
    private String customerIpAddress;

    @Column(name = "reseller_id", nullable = false)
    private String resellerId;

    @Column(name = "reseller_email", length = 255)
    private String resellerEmail;

    @Column(name = "agent_id", columnDefinition = "UUID")
    private UUID agentId;

    @Column(name = "status")
    private Boolean status = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "total_value")
    private Double totalValue = 0.0;

    @Column(name = "referral_code", length = 100)
    private String referralCode;

    @Column(name = "type_of_order", length = 100)
    private String typeOfOrder = "order";

    @Column(name = "iccid_exits", length = 100)
    private String iccidExits;

    @Column(name = "markup_percentage")
    private Double markupPercentage = 0.0;

    @Column(name = "reseller_orderid_name", length = 20)
    private String resellerOrderIdName;

    @Column(name = "reseller_orderid", length = 20)
    private String resellerOrderId;

    @Column(name = "category_discount_data", columnDefinition = "jsonb")
    private String categoryDiscountData;

    @Column(name = "category_discount_percentage")
    private Double categoryDiscountPercentage = 0.0;

    @Column(name = "category_discount_value")
    private Double categoryDiscountValue = 0.0;

    @Column(name = "source_reference_id", length = 50)
    private String sourceReferenceId;

    @Column(name = "source_reference_name", length = 50)
    private String sourceReferenceName;

    @Column(name = "reseller_comment", length = 100)
    private String resellerComment;

    @Column(name = "platform")
    private Integer platform = 1;

    @Column(name = "notification_alert")
    private Boolean notificationAlert = true;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "price_for_customer")
    private Double priceForCustomer = 0.0;

    @Column(name = "tax_for_customer")
    private Double taxForCustomer = 0.0;

    @Column(name = "total_paid_by_customer")
    private Double totalPaidByCustomer = 0.0;

    @Column(name = "freesim_count")
    private Integer freeSimCount = 0;

    // Getters and setters or use Lombok @Getter/@Setter
}
