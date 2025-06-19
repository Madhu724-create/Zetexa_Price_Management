package com.zetexa.entity.Reseller;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Table(name = "reseller_roaming_rates")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResellerRoamingRates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_came")
    private String countryName;

    @Column(name = "operator_name")
    private String operatorName;
    private String tadig;

    @Column(name = "mccmnc")
    private String mccmnc;

    private String moc;
    private BigDecimal mtc;
    private String sms;
    private String data;

    @Column(name = "camel_support")
    private String camelSupport;

    @Column(name = "lte_support")
    private String lteSupport;

    @Column(name = "nsa_5g_support")
    private String nsa5gSupport;

    @Column(name = "voice_charging_interval")
    private Integer voiceChargingInterval;

    @Column(name = "data_charging_interval")
    private Integer dataChargingInterval;

    @Column(name = "currency")
    private String  currency;
}
