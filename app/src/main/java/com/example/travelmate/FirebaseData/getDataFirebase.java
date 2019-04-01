package com.example.travelmate.FirebaseData;

import java.util.ArrayList;

public class getDataFirebase {

    static int RADIUS = 1000000;

    public static ArrayList<String> isInside(double latitude, double longitude, ArrayList<String> temp) {

        ArrayList<Double> distance1 = new ArrayList<>();
        ArrayList<String> distance2 = new ArrayList<String>();
        ArrayList<String> newlist = new ArrayList<>();
        ArrayList<String> newlist1 = new ArrayList<>();

        for (int i = 0; i < temp.size(); i++) {

            newlist.add(addChar(temp.get(i), '.', 2));

        }

        for (int i = 0; i < temp.size(); i++) {
            newlist1.add(addChar(newlist.get(i), '.', 12));
        }

        for (int i = 0; i < newlist1.size(); i++) {

            distance1.add(distance(latitude, longitude, newlist1.get(i)));

        }

        for (int i = 0; i < distance1.size(); i++) {
            if (distance1.get(i) < RADIUS) {
                distance2.add(temp.get(i));
            }

        }

        return distance2;
    }

    private static double distance(double lat1, double lon1, String newlist1) {
        Double lon2, lat2;
        lat2 = Double.valueOf(newlist1.substring(0, 9));
        lon2 = Double.valueOf(newlist1.substring(10, 19));
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        double km = (dist / 0.62137) * 1000;
        return (km);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public static String addChar(String str, char ch, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }

}

