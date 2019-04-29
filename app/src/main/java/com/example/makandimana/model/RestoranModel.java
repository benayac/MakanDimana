package com.example.makandimana.model;

public class RestoranModel {

    private String namaResto, foodType, averagePrice, distance, imgUrl;

    public RestoranModel(String namaResto, String foodType, String averagePrice, String distance, String imgUrl) {
        this.namaResto = namaResto;
        this.averagePrice = averagePrice;
        this.distance = distance;
        this.imgUrl = imgUrl;
        this.foodType = foodType;

    }

    public String getNamaResto() {
        return namaResto;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public String getDistance() {
        return distance;
    }

    public String getImgUrl() { return imgUrl; }

    public String getFoodType(){return foodType; }
}
