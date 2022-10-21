package com.example.demo;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class routeGUI {

    private AnchorPane getRoutePane(Integer LX, Integer LY, String name, String color){
        AnchorPane destPane = new AnchorPane();
        destPane.setLayoutX(LX);
        destPane.setLayoutY(LY);
        destPane.setPrefSize(165, 52);
        destPane.setStyle("-fx-background-color:rgb(254, 254, 254);-fx-background-radius:5px;-fx-border-radius:5px;-fx-border-color:rgb(240, 240, 240);-fx-border-width:1.5px;");
        Label destName = new Label(name);
        destName.setStyle("-fx-text-fill:grey;-fx-font-size:13.4px");
        destName.setLayoutX(37);
        destName.setLayoutY(12);
        destName.setPrefSize(114, 26);
        destName.setFont(Font.font("Myanmar Text"));

        Circle justCircle = new Circle();
        justCircle.setStyle("-fx-stroke-width:1.6px;-fx-fill:rgb(254, 254, 254);-fx-stroke:rgb(" + color + ")");
        justCircle.setRadius(5);
        justCircle.setLayoutX(18);
        justCircle.setLayoutY(26);
        destPane.getChildren().add(justCircle);
        destPane.getChildren().add(destName);
        return destPane;
    }
    private AnchorPane mainPane;
    public AnchorPane getMainPane() {
        return mainPane;
    }
    public void remove(){
        mainPane.getChildren().removeAll();
    }
    public routeGUI(String pickedDate, ConnectSQL mysqlconnect, Integer routeId, String routeOrStopName, String stopName, String time, Integer price, Stage stage1){
        mainPane = new AnchorPane();
        mainPane.setLayoutX(43);
        mainPane.setStyle("-fx-background-color:white;-fx-border-color:rgb(245, 245, 245);-fx-border-width:20px");
        mainPane.setPrefSize(421, 88);
        mainPane.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent mouseEvent) -> {
            Stage stage2 = new Stage();
            AnchorPane root2 = new AnchorPane();
            ScrollPane rootScroll = new ScrollPane(root2);
            stage2.setScene(new Scene(rootScroll, 600, 500));
            stage2.show();
            stage1.hide();
            List stopsList = mysqlconnect.getStopsByRoute(routeId);
            stopsGUI stopsGUI = new stopsGUI(pickedDate, routeOrStopName, stopName, stopsList, time);

            root2.getChildren().add(stopsGUI.getMainPane());
            Button backButton = new Button("\uD83E\uDC60");
            backButton.setStyle("-fx-font-weight:bold;-fx-background-color:rgb(250, 107, 107);-fx-effect:none;-fx-text-fill:black;-fx-background-radius:5px;-fx-label-padding:5px;");
            root2.getChildren().add(backButton);

            backButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
                stage1.show();
                stage2.hide();
            });
        });
        //
        AnchorPane destPane1 = getRoutePane(31, 8, "Ростов-на-Дону", "153,153,153");
        AnchorPane destPane2 = getRoutePane(224, 8, routeOrStopName, "250, 107, 107");
        //
        Line justLine = new Line();
        justLine.setStyle("-fx-stroke:rgb(153, 153, 153)");
        justLine.setStrokeWidth(0.7);
        justLine.setLayoutX(205);
        justLine.setLayoutY(34);
        justLine.setStartX(-8.4);
        justLine.setStartY(-1.14);
        justLine.setEndX(18.2);
        justLine.setEndY(-1.14);
        //
        Label timeLabel = new Label(time);
        timeLabel.setStyle("-fx-text-fill:grey;-fx-font-size:11px;-fx-font-weight:bold");
        timeLabel.setLayoutX(31);
        timeLabel.setLayoutY(57);
        timeLabel.setPrefSize(114, 26);
        timeLabel.setFont(Font.font("Myanmar Text"));
        Label priceLabel = new Label(price + "₽");
        priceLabel.setStyle("-fx-text-fill:rgb(250, 107, 107);-fx-font-size:11px;-fx-font-weight:bold");
        priceLabel.setLayoutX(357);
        priceLabel.setLayoutY(61);
        priceLabel.setPrefSize(41, 26);
        priceLabel.setFont(Font.font("Myanmar Text"));
        //
        mainPane.getChildren().add(timeLabel);
        mainPane.getChildren().add(priceLabel);
        mainPane.getChildren().add(justLine);
        mainPane.getChildren().add(destPane1);
        mainPane.getChildren().add(destPane2);
    }
}
