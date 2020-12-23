package com.wahyurama.moren.Model;

public class Book {

    private String image, carMerk, carType;

    public Book() {
    }

    public Book(String image, String carMerk, String carType) {
        this.image = image;
        this.carMerk = carMerk;
        this.carType = carType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCarMerk() {
        return carMerk;
    }

    public void setCarMerk(String carMerk) {
        this.carMerk = carMerk;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}
