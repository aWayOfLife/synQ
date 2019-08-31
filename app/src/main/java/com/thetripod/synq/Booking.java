package com.thetripod.synq;

public class Booking {
    private String couponNo;
    private String slot;
    private String activity;
    private String status;
    private String eta;

    public Booking() {
    }

    public Booking(String couponNo, String slot, String activity, String status, String eta) {
        this.couponNo = couponNo;
        this.slot = slot;
        this.activity = activity;
        this.status = status;
        this.eta = eta;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }
}
