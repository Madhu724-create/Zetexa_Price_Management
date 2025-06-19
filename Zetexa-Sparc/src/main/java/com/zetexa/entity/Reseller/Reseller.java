package com.zetexa.entity.Reseller;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "customers_reseller", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reseller {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "reseller_id", updatable = false, nullable = false)
    private UUID resellerId;

    @Column(name = "reseller_first_name", length = 250, nullable = false)
    private String resellerFirstName = "";

    @Column(name = "reseller_last_name", length = 250, nullable = false)
    private String resellerLastName = "";

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "alternative_phone", length = 15)
    private String alternativePhone;

    @Column(name = "password")
    private String password;

    @Column(name = "api_key_code", length = 251)
    private String apiKeyCode;

    @Column(name = "currency_code", length = 3)
    private String currencyCode = "USD";

    @Column(name = "subdomain", length = 25)
    private String subdomain;

    @Column(name = "domain", length = 25)
    private String domain;

    @Column(name = "client_ip", length = 250)
    private String clientIp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ResellerCategories category;

    @Column(name = "address")
    private String address;

    @Column(name = "company_name", length = 200)
    private String companyName;

    @Column(name = "company_cin", length = 200)
    private String companyCin;

    @Column(name = "company_tax", length = 200)
    private String companyTax;

    @Column(name = "company_website", length = 250)
    private String companyWebsite;

    @Column(name = "company_country")
    private String companyCountry;

    @Column(name = "pancard_number", length = 15)
    private String pancardNumber;

    @Column(name = "callback_url", length = 200)
    private String callbackUrl;

    @Column(name = "callback_authenication_method", length = 200)
    private String callbackAuthenicationMethod;

    @Column(name = "callback_authenication_token", length = 256)
    private String callbackAuthenicationToken;

    @Column(name = "is_active")
    private boolean isActive = false;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Column(name = "markup", precision = 5, scale = 2)
    private BigDecimal markup = BigDecimal.ZERO;

    @Column(name = "ekyc_for_customer", length = 20)
    private String ekycForCustomer;

    @Column(name = "email_for_customer")
    private boolean emailForCustomer = true;

    @Column(name = "plans_currency", length = 3)
    private String plansCurrency = "USD";

    @Column(name = "discount_percentage")
    private Double discountPercentage = 0.0;

    @Column(name = "breakage_percentage")
    private Double breakagePercentage = 0.0;

    @Column(name = "referral_code", length = 50)
    private String referralCode;

    @Column(name = "restricted_country_check")
    private Boolean restrictedCountryCheck = true;

    @Column(name = "physical_sim")
    private Boolean physicalSim = true;

    @Column(name = "physical_sim_price")
    private Double physicalSimPrice = 0.0;

    @Column(name = "esim_cost")
    private Double esimCost = 0.0;

    @Column(name = "sms_0_flag")
    private Boolean sms0Flag = true;

    @Column(name = "sms_90_flag")
    private Boolean sms90Flag = true;

//    @Lob
    @Column(name = "sms_0_content")
    private String sms0Content;

//    @Lob
    @Column(name = "sms_90_content")
    private String sms90Content;

    @Column(name = "sms_sender_id", length = 20)
    private String smsSenderId;

    @Column(name = "display_voice_sms_plans")
    private Boolean displayVoiceSmsPlans = false;

    @Column(name = "wallet_type", length = 20)
    private String walletType = "RESELLER";

    // You can also add @Column(name = "created_date") and @Column(name = "updated_date") if auditing fields are added later.
}
