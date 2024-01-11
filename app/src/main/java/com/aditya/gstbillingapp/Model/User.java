package com.aditya.gstbillingapp.Model;

public class User {
    private String custemerName;
    private String custemerAddress;
    private String custemerPhone;
    private String custemerGstNo;
    private String custemerEmail;

    public User() {
    }

    public User(String custemerName, String custemerAddress, String custemerPhone, String custemerGstNo, String custemerEmail) {
        this.custemerName = custemerName;
        this.custemerAddress = custemerAddress;
        this.custemerPhone = custemerPhone;
        this.custemerGstNo = custemerGstNo;
        this.custemerEmail = custemerEmail;
    }

    public String getCustemerName() {
        return custemerName;
    }

    public void setCustemerName(String custemerName) {
        this.custemerName = custemerName;
    }

    public String getCustemerAddress() {
        return custemerAddress;
    }

    public void setCustemerAddress(String custemerAddress) {
        this.custemerAddress = custemerAddress;
    }

    public String getCustemerPhone() {
        return custemerPhone;
    }

    public void setCustemerPhone(String custemerPhone) {
        this.custemerPhone = custemerPhone;
    }

    public String getCustemerGstNo() {
        return custemerGstNo;
    }

    public void setCustemerGstNo(String custemerGstNo) {
        this.custemerGstNo = custemerGstNo;
    }

    public String getCustemerEmail() {
        return custemerEmail;
    }

    public void setCustemerEmail(String custemerEmail) {
        this.custemerEmail = custemerEmail;
    }
}
