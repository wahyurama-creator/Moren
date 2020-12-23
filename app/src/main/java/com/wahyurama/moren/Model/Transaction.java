package com.wahyurama.moren.Model;

public class Transaction {

    private String carMerk, carType, date, email, image, transactionID,
            username, price;

    public Transaction() {
    }

    public Transaction(String carMerk, String carType, String date,
                       String email, String image, String transactionID,
                       String username, String price) {
        this.carMerk = carMerk;
        this.carType = carType;
        this.date = date;
        this.email = email;
        this.image = image;
        this.transactionID = transactionID;
        this.username = username;
        this.price = price;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
