package com.example.travelmate.Adapter;

import java.util.ArrayList;

public class TripList {

    ArrayList<TripList> list;

    public TripList() {

    }

    public TripList(ArrayList<TripList> list) {
        this.list = list;
    }

    public ArrayList<TripList> getList() {
        return list;
    }

    public void setList(ArrayList<TripList> list) {
        this.list = list;
    }

}
