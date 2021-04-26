package com.example.weather;

/****
 *  Created by Weather on 18/04/2021
 */

public class Weather {
    public String day;
    public String img;
    public String Maxtemp;
    public String Mintemp;

    public Weather(String day, String img, String maxtemp, String mintemp) {
        this.day = day;
        this.img = img;
        Maxtemp = maxtemp;
        Mintemp = mintemp;
    }
}
