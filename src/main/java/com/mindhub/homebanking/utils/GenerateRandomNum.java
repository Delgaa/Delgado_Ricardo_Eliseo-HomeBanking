package com.mindhub.homebanking.utils;

public class GenerateRandomNum {
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String getRandomNumberCard() {
        return getRandomNumber(1000,10000) + "-" + getRandomNumber(1000,10000) + "-" + getRandomNumber(1000,10000) + "-" + getRandomNumber(1000,10000);
    }

    public int getRandomNumberCVV() {
        return getRandomNumber(100,1000);
    }

    public String getRandomNumberAccount() {
        return "VIN-" + getRandomNumber(1,1000000);
    }
}
