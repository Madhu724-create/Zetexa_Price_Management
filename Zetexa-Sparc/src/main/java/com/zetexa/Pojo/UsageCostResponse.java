package com.zetexa.Pojo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsageCostResponse {
    private String iccid;
    private String mccmnc;
    private double totalPrice;
    private String countyName;
    private String operatorName;

    public UsageCostResponse(String iccid, String mccmnc, double totalPrice,String countyName,String operatorName) {
        this.iccid = iccid;
        this.mccmnc = mccmnc;
        this.totalPrice = totalPrice;
        this.countyName = countyName;
        this.operatorName = operatorName;
    }

}