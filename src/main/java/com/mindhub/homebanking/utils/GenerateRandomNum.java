package com.mindhub.homebanking.utils;

public class GenerateRandomNum {
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
