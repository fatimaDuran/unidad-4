package com.own.sqlite1.model;

/**
 * Created by devtacho on 9/03/18.
 */

public class Product {
    private String idProduct;
    private String name;
    private float price;
    private int stock;

    public Product(String idProduct, String name, int stock,float price) {
        this.idProduct = idProduct;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public Product() {
        this("","", 0, 0.0f);
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProduct='" + idProduct + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
