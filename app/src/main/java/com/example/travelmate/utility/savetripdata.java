package com.example.travelmate.utility;

import java.util.ArrayList;

public class savetripdata {


    String tripname, date;
    ArrayList<String> list;
    ArrayList<String> list1;

    public savetripdata(String tripname, String date, ArrayList<String> list) {
        this.tripname = tripname;
        this.list = list;
        this.date = date;
    }

    public savetripdata(ArrayList<String> list1) {
        this.list1 = list1;
    }

    public ArrayList<String> getList1() {
        return list1;
    }

    public void setList1(ArrayList<String> list1) {
        this.list1 = list1;
    }

    public String getTripname() {
        return tripname;
    }

    public void setTripname(String tripname) {
        this.tripname = tripname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }


}
