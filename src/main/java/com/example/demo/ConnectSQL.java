package com.example.demo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

public class ConnectSQL {
    public ConnectSQL(){
        conn = ConnectDb();
        try (BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\user\\IdeaProjects\\demo\\src\\main\\resources\\com\\example\\demo\\files_for_db\\city_names copy.txt"), "UTF8"))) {
            PreparedStatement statement;
            String sCurrentLine;
            statement = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
            statement.execute();
            statement = conn.prepareStatement("TRUNCATE TABLE destination;");
            statement.execute();
            statement = conn.prepareStatement("TRUNCATE TABLE routes;");
            statement.execute();
            statement = conn.prepareStatement("TRUNCATE TABLE trips;");
            statement.execute();
            statement = conn.prepareStatement("TRUNCATE TABLE trip_days;");
            statement.execute();
            statement = conn.prepareStatement("TRUNCATE TABLE stops;");
            statement.execute();
            while ((sCurrentLine = br1.readLine()) != null) {
                statement = conn.prepareStatement("INSERT INTO destination(name) VALUES('" + sCurrentLine + "');");
                statement.execute();
            }
            BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\user\\IdeaProjects\\demo\\src\\main\\resources\\com\\example\\demo\\files_for_db\\day_names.txt")));
            while ((sCurrentLine = br2.readLine()) != null) {
                statement = conn.prepareStatement("INSERT INTO trip_days(name) VALUES('" + sCurrentLine + "');");
                statement.execute();
            }
            BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\user\\IdeaProjects\\demo\\src\\main\\resources\\com\\example\\demo\\files_for_db\\routes copy.txt")));
            while ((sCurrentLine = br3.readLine()) != null) {
                statement = conn.prepareStatement("INSERT INTO routes(route_id, `from`, `to`, days, time) VALUES(" + sCurrentLine + ");");
                statement.execute();
            }
            BufferedReader br4 = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\user\\IdeaProjects\\demo\\src\\main\\resources\\com\\example\\demo\\files_for_db\\trips copy.txt")));
            while ((sCurrentLine = br4.readLine()) != null) {
                statement = conn.prepareStatement("INSERT INTO trips(trip_id, departure_time, arrival_time, routes_id, price) VALUES(" + sCurrentLine + ");");
                statement.execute();
            }
            BufferedReader br5 = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\user\\IdeaProjects\\demo\\src\\main\\resources\\com\\example\\demo\\files_for_db\\stops copy.txt")));
            while ((sCurrentLine = br5.readLine()) != null) {
                if (!sCurrentLine.equals(""))
                {
                    statement = conn.prepareStatement("INSERT INTO stops(stop_id, name, route_id, arrival_time, departure_time, total_time, stop_price) VALUES(" + sCurrentLine + ");");
                    statement.execute();
                }
            }
            statement = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1;");
            statement.execute();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    Connection conn = null;
    public static Connection ConnectDb(){
        try {
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/test_schema","root","sds2013");
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }

    }
    public static ObservableList<Routes> getDataRoutes(){
        Connection conn = ConnectDb();
        ObservableList<Routes> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT trip_id, destination_id, route_id, name, days, trips.departure_time, trips.arrival_time, price  FROM destination JOIN `routes` ON `routes`.`to`=`destination`.`destination_id` " +
                    "INNER JOIN `trips` ON  `routes`.`route_id`=`trips`.`routes_id`;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                list.add(new Routes(Integer.parseInt(rs.getString("trip_id")), Integer.parseInt(rs.getString("destination_id")), Integer.parseInt(rs.getString("route_id")), rs.getString("name"), rs.getString("days"), rs.getString("trips.departure_time"), rs.getString("trips.arrival_time"), rs.getInt("price")));
            }
        } catch (Exception e) {
        }
        return list;
    }
    public static ObservableList<String> getNames(){
        Connection conn = ConnectDb();
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT name FROM destination;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                list.add(rs.getString("name"));
            }
        } catch (Exception e) {
        }
        return list;
    }
    public static ObservableList<String> getNamesWithStops(){
        Connection conn = ConnectDb();
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT destination.name FROM destination WHERE destination.name NOT IN (SELECT stops.name FROM stops) UNION ALL SELECT DISTINCT stops.name FROM stops;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                list.add(rs.getString("name"));
            }
        } catch (Exception e) {
        }
        return list;
    }

    public static List getStopsByRoute(Integer route){
        Connection conn = ConnectDb();
        List<ArrayList<String>> list=new ArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT stops.route_id, stops.stop_id, stops.name, stops.arrival_time, stops.departure_time, stops.total_time FROM destination JOIN `routes` ON `routes`.`to`=`destination`.`destination_id` INNER JOIN `stops` ON `routes`.`route_id`=`stops`.`route_id` WHERE stops.route_id = " + route + ";");
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                list.get(0).add(rs.getString("stops.name"));
                list.get(1).add(rs.getString("stops.arrival_time"));
                list.get(2).add(rs.getString("stops.departure_time"));
                list.get(3).add(rs.getString("stops.total_time"));
            }
        } catch (Exception e) {
        }
        return list;
    }
    public List getStopsCountAndTotalPrice(Integer route){
        Integer count = 0;
        Integer price = 0;
        List list = new ArrayList<>();
        Connection conn = ConnectDb();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT count(stop_id) AS c, price FROM stops INNER JOIN trips ON trips.routes_id = stops.route_id WHERE stops.route_id = " + route + ";");
            ResultSet rs = null;
            rs = ps.executeQuery();
            while (rs.next()){
                price = Integer.parseInt(rs.getString("price"));
                count = Integer.parseInt(rs.getString("c"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        list.add(count);
        list.add(price);
        return list;
    }
    public Integer getPrice(String totalTime, Integer route){
        LocalTime localTime = LocalTime.parse(totalTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        int hour = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
        if (hour >= 24)
            hour = hour % 24;
        int minute = localTime.get(ChronoField.MINUTE_OF_HOUR);
        int total = hour * 60 + minute;
        List list = getStopsCountAndTotalPrice(route);
        Double totalCount = Double.valueOf(Integer.parseInt(String.valueOf(list.get(0))));
        Double totalPrice = Double.valueOf(Integer.parseInt(String.valueOf(list.get(1))));
        Double stopPrice = Double.valueOf(totalPrice / totalCount * total / 100);
        if (stopPrice > totalPrice)
            stopPrice = totalPrice;
        return Math.toIntExact(Math.round(stopPrice));
    }
    public static ObservableList<Stops> getStopsListByRoute(Integer route){
        Connection conn = ConnectDb();
        ObservableList <Stops> list = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = conn.prepareStatement("SELECT routes.route_id, stop_id, stops.name, stops.arrival_time, stops.departure_time, total_time, stop_price FROM routes INNER JOIN `stops` ON  `routes`.`route_id`=`stops`.`route_id` WHERE stops.route_id=" + route);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                list.add(new Stops(Integer.parseInt(rs.getString("stop_id")), Integer.parseInt(rs.getString("route_id")), rs.getString("name"), rs.getString("arrival_time"), rs.getString("departure_time"), rs.getString("total_time"), Integer.parseInt(rs.getString("stop_price"))));
            }
        }
        catch (Exception e){}
        return list;
    }
    public static List getRouteByName(String name){
        Connection conn = ConnectDb();
        List<ArrayList<String>> list=new ArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT name, departure_time, arrival_time, days, price, route_id FROM destination JOIN `routes` ON `routes`.`to`=`destination`.`destination_id` INNER JOIN `trips` ON `routes`.`route_id`=`trips`.`routes_id` WHERE name='" + name + "';");
            ResultSet rs = ps.executeQuery();
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            while (rs.next()){
                list.get(0).add(rs.getString("days"));
                list.get(1).add(rs.getString("departure_time"));
                list.get(2).add(rs.getString("arrival_time"));
                list.get(3).add(rs.getString("price"));
                list.get(4).add(rs.getString("route_id"));
                list.get(5).add(rs.getString("name"));
            }
        } catch (Exception e) {
        }
        return list;
    }
    public List getRouteByStopName(String name){
        Connection conn = ConnectDb();
        List<ArrayList<String>> list=new ArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT routes.days, destination.name, routes.time, stops.arrival_time, stops.stop_price, routes.route_id FROM destination JOIN routes ON routes.`to`=destination.destination_id INNER JOIN stops ON routes.route_id = stops.route_id WHERE stops.name = '" + name + "'; ");
            ResultSet rs = ps.executeQuery();
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            while (rs.next()){
                list.get(0).add(rs.getString("days"));
                list.get(1).add(rs.getString("time"));
                list.get(2).add(rs.getString("arrival_time"));
                list.get(3).add(rs.getString("stop_price"));
                list.get(4).add(rs.getString("route_id"));
                list.get(5).add(rs.getString("name"));
            }
        } catch (Exception e) {
        }
        return list;
    }
}
