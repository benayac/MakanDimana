package com.example.makandimana.model;

public class RestoranModel {

    private String namaResto, foodType, imgUrl;
    private double langitude, longitude;
    private int minPrice, maxPrice;

    public RestoranModel(String namaResto, String foodType, String imgUrl, double langitude, double longitude, int minPrice, int maxPrice) {
        this.namaResto = namaResto;
        this.imgUrl = imgUrl;
        this.foodType = foodType;
        this.langitude = langitude;
        this.longitude = longitude;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;

    }

    public String getNamaResto() {
        return namaResto;
    }

    public double getLangitude() {
        return langitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public String getImgUrl() { return imgUrl; }

    public String getFoodType(){return foodType; }
}
