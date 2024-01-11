package com.aditya.gstbillingapp.Model;

public class Bill {
    private String inviceId;
    private String custemerName, custemerAdderss,custemerPhone,custemerEmail,custemerGstno;
    private String productName, productPrice, productHSNSACno, productQuantity, productCGst, productSgst;

    private String billData;

    public Bill() {
    }


    public String getBillData() {
        return billData;
    }

    public void setBillData(String billData) {
        this.billData = billData;
    }

    public String getInviceId() {
        return inviceId;
    }

    public void setInviceId(String inviceId) {
        this.inviceId = inviceId;
    }

    public String getCustemerName() {
        return custemerName;
    }

    public void setCustemerName(String custemerName) {
        this.custemerName = custemerName;
    }

    public String getCustemerAdderss() {
        return custemerAdderss;
    }

    public void setCustemerAdderss(String custemerAdderss) {
        this.custemerAdderss = custemerAdderss;
    }

    public String getCustemerPhone() {
        return custemerPhone;
    }

    public void setCustemerPhone(String custemerPhone) {
        this.custemerPhone = custemerPhone;
    }

    public String getCustemerEmail() {
        return custemerEmail;
    }

    public void setCustemerEmail(String custemerEmail) {
        this.custemerEmail = custemerEmail;
    }

    public String getCustemerGstno() {
        return custemerGstno;
    }

    public void setCustemerGstno(String custemerGstno) {
        this.custemerGstno = custemerGstno;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductHSNSACno() {
        return productHSNSACno;
    }

    public void setProductHSNSACno(String productHSNSACno) {
        this.productHSNSACno = productHSNSACno;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductCGst() {
        return productCGst;
    }

    public void setProductCGst(String productCGst) {
        this.productCGst = productCGst;
    }

    public String getProductSgst() {
        return productSgst;
    }

    public void setProductSgst(String productSgst) {
        this.productSgst = productSgst;
    }
}
