package com.aditya.gstbillingapp.Model;

public class MyProducts {
    private String productName;
    private String productPrice;
    private String productQuantity;
    private String productSANno;

    public MyProducts() {
    }

    public MyProducts(String productName, String productPrice, String productQuantity, String productSANno) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSANno = productSANno;
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

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductSANno() {
        return productSANno;
    }

    public void setProductSANno(String productSANno) {
        this.productSANno = productSANno;
    }
}
