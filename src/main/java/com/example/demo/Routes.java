package com.example.demo;

public class Routes {

    int route_id, dest_id, trip_id, price;
    String name, days, deptime , arrtime;

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public void setDest_id(int dest_id) {
        this.dest_id = dest_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setDeptime(String deptime) {
        this.deptime = deptime;
    }

    public void setArrtime(String arrtime) {
        this.arrtime = arrtime;
    }

    public int getRoute_id() {
        return route_id;
    }

    public int getDest_id() {
        return dest_id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDays() {
        return days;
    }

    public String getDeptime() {
        return deptime;
    }

    public String getArrtime() {
        return arrtime;
    }

    public Routes(int trip_id, int dest_id, int route_id, String name, String days, String deptime, String arrtime, Integer price) {
        this.route_id = route_id;
        this.dest_id = dest_id;
        this.trip_id = trip_id;
        this.name = name;
        this.days = days;
        this.deptime = deptime;
        this.arrtime = arrtime;
        this.price = price;
    }
}