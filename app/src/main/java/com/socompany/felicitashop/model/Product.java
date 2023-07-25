package com.socompany.felicitashop.model;

public class Product {
    private String productID;
    private String productName;
    private String productPrice;
    private String productDescription;
    private String productCategory;
    private String productData;
    private String productImage;
    private String productTime;

    public Product(String pid, String pname, String price, String description,
                   String category, String data, String image, String time) {
        this.productID = pid;
        this.productName = pname;
        this.productPrice = price;
        this.productDescription = description;
        this.productCategory = category;
        this.productData = data;
        this.productImage = image;
        this.productTime = time;
    }
    public Product(){}

    public Product(String productID, String productName) {
        this.productID = productID;
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductData() {
        return productData;
    }

    public void setProductData(String productData) {
        this.productData = productData;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }
}
