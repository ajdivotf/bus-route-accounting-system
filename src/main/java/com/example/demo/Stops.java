package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;

public class Stops {
    Integer stopsId, routeId, price;
    String stopName, arrivalTime, departureTime, totalTime;

    public Integer getStopsId() {
        return stopsId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public Integer getPrice() {
        return price;
    }

    public String getStopName() {
        return stopName;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String countTotalTime(String firstTime, String secondTime) {
        LocalTime localTime = LocalTime.parse(firstTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        int hour = - localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
        int minute = - localTime.get(ChronoField.MINUTE_OF_HOUR);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            calendar.setTime(sdf.parse(secondTime));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.add(Calendar.HOUR, hour);
        calendar.add(Calendar.MINUTE, minute);
        return sdf.format(calendar.getTime());
    }
    public Stops(){};
    public Stops(Integer stopsId, Integer routeId, String stopName, String arrivalTime, String departureTime, String totalTime, Integer price){
        this.stopsId = stopsId;
        this.routeId = routeId;
        this.stopName = stopName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.totalTime = totalTime;
        this.price = price;
    }
}
