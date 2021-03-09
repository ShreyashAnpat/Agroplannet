package com.cctpl.agroplannet.Model;

public class OrderData {
   String  MRP ,ProductCount,ProductDetails,ProductImage1,ProductImage2,ProductImage3 , ProductMeasurement ,ProductOriginalPrice,TotalPrice;

    public OrderData(String MRP, String productCount, String productDetails, String productImage1, String productImage2, String productImage3, String productMeasurement, String productOriginalPrice, String totalPrice) {
        this.MRP = MRP;
        ProductCount = productCount;
        ProductDetails = productDetails;
        ProductImage1 = productImage1;
        ProductImage2 = productImage2;
        ProductImage3 = productImage3;
        ProductMeasurement = productMeasurement;
        ProductOriginalPrice = productOriginalPrice;
        TotalPrice = totalPrice;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getProductCount() {
        return ProductCount;
    }

    public void setProductCount(String productCount) {
        ProductCount = productCount;
    }

    public String getProductDetails() {
        return ProductDetails;
    }

    public void setProductDetails(String productDetails) {
        ProductDetails = productDetails;
    }

    public String getProductImage1() {
        return ProductImage1;
    }

    public void setProductImage1(String productImage1) {
        ProductImage1 = productImage1;
    }

    public String getProductImage2() {
        return ProductImage2;
    }

    public void setProductImage2(String productImage2) {
        ProductImage2 = productImage2;
    }

    public String getProductImage3() {
        return ProductImage3;
    }

    public void setProductImage3(String productImage3) {
        ProductImage3 = productImage3;
    }

    public String getProductMeasurement() {
        return ProductMeasurement;
    }

    public void setProductMeasurement(String productMeasurement) {
        ProductMeasurement = productMeasurement;
    }

    public String getProductOriginalPrice() {
        return ProductOriginalPrice;
    }

    public void setProductOriginalPrice(String productOriginalPrice) {
        ProductOriginalPrice = productOriginalPrice;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public OrderData() {
    }
}
