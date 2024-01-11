package com.aditya.gstbillingapp.Model;

public class Products {
    private String productName;
    private String price;
    private String HSNSACno;

    public Products() {
    }

    public Products(String productName, String price, String HSNSACno) {
        this.productName = productName;
        this.price = price;
        this.HSNSACno = HSNSACno;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getHSNSACno() {
        return HSNSACno;
    }

    public void setHSNSACno(String HSNSACno) {
        this.HSNSACno = HSNSACno;
    }
}
