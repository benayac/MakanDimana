package com.example.makandimana.model;

public class menuMakananModel {

    private String foodName;
    private int foodPrice;
    private String imgUrl;

    public menuMakananModel(){

    }

    public menuMakananModel(String foodName, int foodPrice, String imgUrl){
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getFoodPrice() {
        return foodPrice;
    }
}
