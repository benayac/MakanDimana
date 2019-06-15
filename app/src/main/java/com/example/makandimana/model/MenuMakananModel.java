package com.example.makandimana.model;

public class MenuMakananModel {

    //berikut merupakan class model yang berisi property tiap menu makanan

    private String foodName;
    private int foodPrice;
    private String imgUrl;

    public MenuMakananModel(){

    }

    public MenuMakananModel(String foodName, int foodPrice, String imgUrl){
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
