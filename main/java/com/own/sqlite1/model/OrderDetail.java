package com.own.sqlite1.model;

/**
 * Created by Marili Arevalo on 23/02/2017.
 */

public class OrderDetail {
    private String idOrderHeader;
    private int sequence;
    private String idProduct;
    private int quantity;
    private float price;

    public OrderDetail(String idOrderHeader, int sequence, String idProduct, int quantity, float price) {
        this.idOrderHeader = idOrderHeader;
        this.sequence = sequence;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.price = price;
    }
    public OrderDetail(){
        this("",0,"",0,0.0F);
    }

    public String getIdOrderHeader() {
        return idOrderHeader;
    }

    public void setIdOrderHeader(String idOrderHeader) {
        this.idOrderHeader = idOrderHeader;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "idOrderHeader='" + idOrderHeader + '\'' +
                ", sequence=" + sequence +
                ", idProduct='" + idProduct + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
