package com.example.makandimana.model;

public class RestoranModel {

    //berikut merupakan class model yang berisi property tiap restoran

    private String namaResto, foodType, imgUrl;
    private double langitude, longitude;
    private int minPrice, maxPrice;
    private String openHr, closeHr;

    public RestoranModel() {

    }

    public RestoranModel(String namaResto, String foodType, String imgUrl, double langitude, double longitude, int minPrice, int maxPrice, String openHr, String closeHr) {
        this.namaResto = namaResto;
        this.imgUrl = imgUrl;
        this.foodType = foodType;
        this.langitude = langitude;
        this.longitude = longitude;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.openHr = openHr;
        this.closeHr = closeHr;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public String getFoodType() {
        return foodType;
    }

}
