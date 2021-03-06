package com.rahulkumaryadav.pdd1;


import com.google.firebase.Timestamp;

import java.util.Date;

public class RewardModel {
   private String type;
   private String lowerLimit;
   private String upperLimit;
   private String discORamt;
   private String couponBody;
   private Date timestamp;
   private Boolean alreadyUsed;
   private String couponID;

    public RewardModel(String couponID,String type, String lowerLimit, String upperLimit, String discORamt, String couponBody, Date timestamp,Boolean alreadyUsed) {
        this.couponID=couponID;
        this.type = type;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.discORamt = discORamt;
        this.couponBody = couponBody;
        this.timestamp = timestamp;
        this.alreadyUsed=alreadyUsed;
    }

    public String getCouponID() {
        return couponID;
    }

    public void setCouponID(String couponID) {
        this.couponID = couponID;
    }

    public Boolean getAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(Boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getDiscORamt() {
        return discORamt;
    }

    public void setDiscORamt(String discORamt) {
        this.discORamt = discORamt;
    }

    public String getCouponBody() {
        return couponBody;
    }

    public void setCouponBody(String couponBody) {
        this.couponBody = couponBody;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
