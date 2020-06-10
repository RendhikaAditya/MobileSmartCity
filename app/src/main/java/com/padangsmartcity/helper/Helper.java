package com.padangsmartcity.helper;

import android.location.Location;

import java.util.Date;

public class Helper {

    public static String waktu(){
        String waktu = null;
        Date date = new Date();
        int jam = date.getHours();

        if (jam >= 0 && jam <= 9){
            waktu = "Pagi";
        }else if(jam > 9 && jam <= 14){
            waktu = "Siang";
        }else if(jam > 14 && jam <= 18){
            waktu = "Sore";
        }else if(jam > 18 && jam <= 24){
            waktu = "Malam";
        }
        return waktu;
    }

    public String getDistance(double lat1, double lng1, double lat2, double lng2){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);


        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);

        Float distanceinmeter = loc1.distanceTo(loc2);
        if (distanceinmeter >= 1000){
            return String.format("%.2f", distanceinmeter/1000)+"KM";
        }else{
            return String.format("%.2f", distanceinmeter)+"M";
        }
    }

}
