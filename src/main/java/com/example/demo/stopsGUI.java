package com.example.demo;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class stopsGUI {
    private AnchorPane mainPane;
    private String destRouteName;
    private String destStopName;
    private List stopName, stopArrTime, stopDepTime, stopTotalTime;
    private Integer countStops = 0;
    private String arrTime;
    private String depTime;

    public AnchorPane getMainPane() {
        return mainPane;
    }

    private AnchorPane topPart(){
        AnchorPane topPane = new AnchorPane();
        topPane.setLayoutX(58);
        topPane.setLayoutY(14);
        Label station = new Label("Станция");
        station.setStyle("-fx-text-fill:rgb(174, 174, 174);-fx-font-family:Segoe UI;-fx-font-size:10px");
        station.setLayoutX(0);
        station.setLayoutY(0);
        //
        Label arrTime = new Label("Прибытие");
        arrTime.setStyle("-fx-text-fill:rgb(174, 174, 174);-fx-font-family:Segoe UI;-fx-font-size:10px");
        arrTime.setLayoutX(295);
        arrTime.setLayoutY(0);
        //
        Label depTime = new Label("Отправление");
        depTime.setStyle("-fx-text-fill:rgb(174, 174, 174);-fx-font-family:Segoe UI;-fx-font-size:10px");
        depTime.setLayoutX(370);
        depTime.setLayoutY(0);
        //
        Label totalTime = new Label("Время в пути");
        totalTime.setStyle("-fx-text-fill:rgb(174, 174, 174);-fx-font-family:Segoe UI;-fx-font-size:10px");
        totalTime.setLayoutX(466);
        totalTime.setLayoutY(0);
        //
        Line justLine = new Line();
        justLine.setStyle("-fx-stroke-width:0.4px");
        justLine.setStroke(Paint.valueOf("rgb(174, 174, 174)"));
        justLine.setStartX(0);
        justLine.setStartY(22);
        justLine.setEndX(526.4);
        justLine.setEndY(22);
        //
        topPane.getChildren().add(station);
        topPane.getChildren().add(totalTime);
        topPane.getChildren().add(depTime);
        topPane.getChildren().add(arrTime);
        topPane.getChildren().add(justLine);
        return topPane;
    }
    private AnchorPane stopsPart(){
        AnchorPane stopsPane = new AnchorPane();
        stopsPane.setLayoutX(64);
        stopsPane.setLayoutY(82);
        Label from = new Label("Ростов-на-Дону");
        from.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px;-fx-font-weight:bold");
        from.setLayoutX(0);
        from.setLayoutY(0);
        //
        if (destStopName.equals(destRouteName))
        {
            Integer index = 1;
            for (Object o : stopName){
                Label stopName = new Label(String.valueOf(o));
                stopName.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px");
                stopName.setLayoutX(0);
                stopName.setLayoutY(39 * index);
                stopsPane.getChildren().add(stopName);
                index++;
            };
            countStops = index;
            Label to = new Label(destRouteName);
            to.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px;-fx-font-weight:bold");
            to.setLayoutX(0);
            to.setLayoutY(39 * index);
            to.setPadding(new Insets(0, 0, 30, 0));
            stopsPane.getChildren().add(from);
            stopsPane.getChildren().add(to);
        }
        else{
            Integer index = 1;
            for (Object o : stopName){
                if (!o.equals(destStopName))
                {
                    Label stopName = new Label(String.valueOf(o));
                    stopName.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px");
                    stopName.setLayoutX(0);
                    stopName.setLayoutY(39 * index);
                    stopsPane.getChildren().add(stopName);
                    index++;
                }
                else break;
            };
            countStops = index;
            Label to = new Label(destStopName);
            to.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px;-fx-font-weight:bold");
            to.setLayoutX(0);
            to.setLayoutY(39 * index);
            to.setPadding(new Insets(0, 0, 30, 0));
            stopsPane.getChildren().add(from);
            stopsPane.getChildren().add(to);
        }
        return stopsPane;
    }
    private AnchorPane timePart(){
        AnchorPane timePane = new AnchorPane();
        timePane.setLayoutX(264);
        timePane.setLayoutY(82);
        Label dashFrom = new Label("—");
        dashFrom.setLayoutX(127);
        Label dashTo = new Label("—");
        dashTo.setLayoutX(215);
        dashTo.setLayoutY(countStops * 39);
        Label dashTotal = new Label("—");
        dashTotal.setLayoutX(310);
        dashTotal.setLayoutY(0);
        for(int i = 1; i < countStops; i++){
            String arrTime = String.valueOf(stopArrTime.get(i - 1));
            Label dashTime1 = new Label(arrTime.substring(0, arrTime.length() - 3));
            dashTime1.setLayoutX(110);
            dashTime1.setLayoutY(i * 39);
            String depTime = String.valueOf(stopDepTime.get(i - 1));
            Label dashTime2 = new Label(depTime.substring(0, depTime.length() - 3));
            dashTime2.setLayoutX(200);
            dashTime2.setLayoutY(i * 39);
            timePane.getChildren().add(dashTime1);
            timePane.getChildren().add(dashTime2);
            //
            String totalTime = String.valueOf(stopTotalTime.get(i - 1));
            LocalTime localTime = LocalTime.parse(totalTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
            int hour = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
            int minute = localTime.get(ChronoField.MINUTE_OF_HOUR);
            Label dashTime3;
            if (hour == 0)
                dashTime3 = new Label(minute + " мин");
            else dashTime3 = new Label(hour + " ч " + minute + " мин");
            dashTime3.setLayoutX(-5.2 * dashTime3.getText().length() + 320);
            dashTime3.setLayoutY(i * 39);
            timePane.getChildren().add(dashTime3);
        }
        Label depTime = new Label(this.depTime);
        depTime.setLayoutX(200);
        depTime.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px;-fx-font-weight:bold;-fx-text-fill:rgb(250, 107, 107)");
        Label arrTime = new Label(this.arrTime);
        arrTime.setLayoutX(110);
        arrTime.setLayoutY(countStops * 39);
        arrTime.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px;-fx-font-weight:bold;-fx-text-fill:rgb(250, 107, 107)");
        //
        Stops stops = new Stops();
        String sTotalRouteTime = stops.countTotalTime(this.arrTime + ":00", this.depTime + ":00");
        LocalTime localTime = LocalTime.parse(sTotalRouteTime + ":00", DateTimeFormatter.ofPattern("HH:mm:ss"));
        int hour = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
        int minute = localTime.get(ChronoField.MINUTE_OF_HOUR);
        Label totalRouteTime;
        if (hour == 0)
            totalRouteTime = new Label(minute + " мин");
        else totalRouteTime = new Label(hour + " ч " + minute + " мин");
        totalRouteTime.setLayoutX(-5.2 * totalRouteTime.getText().length() + 320);
        totalRouteTime.setLayoutY(countStops * 39);
        totalRouteTime.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px;-fx-text-fill:black");
        //
        timePane.getChildren().add(totalRouteTime);
        timePane.getChildren().add(dashTotal);
        timePane.getChildren().add(depTime);
        timePane.getChildren().add(arrTime);
        timePane.getChildren().add(dashTo);
        timePane.getChildren().add(dashFrom);
        return timePane;
    }
    public String getDate(String date){
        Month month = Month.of(Integer.parseInt(date.substring(5, 7)));
        Locale loc = Locale.forLanguageTag("ru");
        String realMonth = month.getDisplayName(TextStyle.FULL_STANDALONE, loc);
        realMonth = realMonth.substring(0, realMonth.length() - 1) + "я";
        String day = date.substring(8);
        return Integer.parseInt(day) + " " + realMonth;
    }
    public stopsGUI(String pickedDate, String destRouteName, String destStopName, List stopsList, String time) {
        this.destRouteName = destRouteName;
        this.destStopName = destStopName;
        this.stopName = (List) stopsList.get(0);
        this.stopArrTime = (List) stopsList.get(1);
        this.stopDepTime = (List) stopsList.get(2);
        this.stopTotalTime = (List) stopsList.get(3);
        this.depTime = time.substring(0, time.length() - 8);
        this.arrTime = time.substring(8);
        mainPane = new AnchorPane();
        Label day = new Label(getDate(pickedDate));
        day.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px");
        day.setLayoutX(63);
        day.setLayoutY(56);
        Label dot = new Label("·");
        dot.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px");
        dot.setLayoutX(153);
        dot.setLayoutY(54);
        Label moscowTime = new Label("Московское время");
        moscowTime.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px");
        moscowTime.setLayoutX(193);
        moscowTime.setLayoutY(56);
        AnchorPane stopsPane = new AnchorPane();
        stopsPane.getChildren().add(day);
        stopsPane.getChildren().add(dot);
        stopsPane.getChildren().add(moscowTime);
        //
        stopsPane.getChildren().add(stopsPart());
        stopsPane.getChildren().add(timePart());
        stopsPane.getChildren().add(topPart());
        stopsPane.setLayoutY(55);
        //
        AnchorPane routeNamePane = new AnchorPane();
        Label routeNameLabel = new Label("Маршрут автобуса Ростов-на-Дону — " + destRouteName);
        routeNameLabel.setStyle("-fx-font-family:Segoe UI;-fx-font-size:19px;-fx-font-weight:bold");
        routeNamePane.setLayoutY(15);
        routeNamePane.setLayoutX(58);
        routeNamePane.getChildren().add(routeNameLabel);
        //
        mainPane.getChildren().add(routeNamePane);
        mainPane.getChildren().add(stopsPane);
    }
}
