package com.ecobee.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ClimateComparison{
    public static void main(String[] args){
        java.util.Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                EcobeeTempData ecobeeTempData = new EcobeeTempData();
                System.out.println(ecobeeTempData.pollData());
            }
        };
        timer.schedule(timerTask, 0, 900000);

    }
    public String readWeatherTemp(){

        String weatherTemp1 = "";
        try {
            FileReader fileReader = new FileReader("WeatherTemp.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            weatherTemp1 = bufferedReader.readLine();
            System.out.println(weatherTemp1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherTemp1;
    }


}
