package com.mindhub.homebanking.utils;

public class GenerateRandomNum {
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String getRandomNumberCard() {
        return getRandomNumber(4000,6000) + "-" + getRandomNumber(1000,10000) + "-" + getRandomNumber(1000,10000) + "-" + getRandomNumber(1000,10000);
    }

    public String getRandomNumberCVV() {
        int random = getRandomNumber(1,1000);

        if (random < 10){
            return "00" + random;
        }

        if (random < 100){
            return "0" + random;
        }

        return String.valueOf(random);
    }

    public String getRandomNumberAccount() {
        int random = getRandomNumber(1,100000000);

        if (random < 10){
            return "VIN-0000000" + random;
        }

        if (random < 100){
            return "VIN-000000" + random;
        }

        if (random < 1000){
            return "VIN-00000" + random;
        }

        if (random < 10000){
            return "VIN-0000" + random;
        }

        if (random < 100000){
            return "VIN-000" + random;
        }

        if (random < 1000000){
            return "VIN-00" + random;
        }

        if (random < 10000000){
            return "VIN-0" + random;
        }

        return "VIN-" + random;
    }
}
