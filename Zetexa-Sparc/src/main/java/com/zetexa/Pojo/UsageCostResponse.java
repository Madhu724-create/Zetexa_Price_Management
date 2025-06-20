package com.zetexa.Pojo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsageCostResponse {
    private String iccid;
    private String mccmnc;
    private double totalCost;
    private String countyName;
    private String operatorName;
    private String parentResellerName = "Zetexa";
    private String ResellerName ;
    private String data;
    private String packageID;

    public UsageCostResponse(String iccid, String mccmnc, double totalCost, String countyName, String operatorName, String parentResellerName, String resellerName, String data, String packageID) {
        this.iccid = iccid;
        this.mccmnc = mccmnc;
        this.totalCost = totalCost;
        this.countyName = countyName;
        this.operatorName = operatorName;
        this.parentResellerName = parentResellerName;
        ResellerName = resellerName;
        this.data = data;
        this.packageID = packageID;
    }
}