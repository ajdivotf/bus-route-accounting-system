package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import static javafx.scene.paint.Color.rgb;

public class Main extends Application{
    private static Stage mainStage;
    private static Stage signInStage;

    public static Stage getSignInStage() {
        return signInStage;
    }

    public static void setSignInStage(Stage signInStage) {
        Main.signInStage = signInStage;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        Main.mainStage = mainStage;
    }

    public static void main(String[] args) {

        Application.launch(args);
    }
    public String getDate(String date){
        Month month = Month.of(Integer.parseInt(date.substring(5, 7)));
        Locale loc = Locale.forLanguageTag("ru");
        String realMonth = month.getDisplayName(TextStyle.FULL_STANDALONE, loc);
        realMonth = realMonth.substring(0, realMonth.length() - 1) + "я";
        String day = date.substring(8);
        return Integer.parseInt(day) + " " + realMonth;
    }
    private String getPickedDate(){
        if (datePicker.getValue() != null)
            return datePicker.getValue().toString();
        else
            return String.valueOf(LocalDate.now());
    }
    private Integer getDayOfWeek(DatePicker datePicker){

        Calendar cal = Calendar.getInstance();
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse(getPickedDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        cal.setTime(d);
        Integer day = cal.get(Calendar.DAY_OF_WEEK);
        if (day > 1)
            day--;
        else day = 7;
        return day;
    }
    private DatePicker datePicker;
    private Integer dayOfWeek = 0;
    ObservableList<String> listM;
    @Override
    public void start(Stage stage) {
        setMainStage(stage);
        Label signIn = new Label("Войти");
        signIn.setStyle("-fx-font-family:Segoe UI;-fx-font-size:12px");
        signIn.setLayoutX(538);
        signIn.setLayoutY(27);
        signIn.setId("sign-in-button");
        signIn.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            Parent root3 = null;
            try {
                root3 = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage3 = new Stage();
            Scene scene3 = new Scene(root3);
            String stylesheet = getClass().getResource("styles.css").toExternalForm();
            scene3.getStylesheets().add(stylesheet);
            stage3.setScene(scene3);
            setSignInStage(stage3);
            stage3.show();
            stage.hide();
        });
        //
        Label nameLabel = new Label("Расписание междугородного транспорта");
        nameLabel.setStyle("-fx-font-family:Segoe UI;-fx-font-size:22px");
        nameLabel.setLayoutX(73);
        nameLabel.setLayoutY(68);
        Label txtLabel = new Label("Автобусные рейсы");
        txtLabel.setStyle("-fx-font-family:Segoe UI;-fx-font-size:13px");
        txtLabel.setLayoutX(376);
        txtLabel.setLayoutY(96);
        Label fromLabel = new Label("Ростов-на-Дону");
        fromLabel.setStyle("-fx-font-family:Segoe UI;-fx-font-size:13px;-fx-pref-height:37px;-fx-pref-width:150px;-fx-background-color:white;-fx-border-color:rgb(241, 242, 246);-fx-border-width:1px;-fx-border-radius:3px;-fx-padding:8px;-fx-background-radius:3px");
        fromLabel.setLayoutX(59);
        fromLabel.setLayoutY(184);
        //
        ConnectSQL mysqlconnect = new ConnectSQL();
        listM = mysqlconnect.getNamesWithStops();
        ComboBox<String> toBox = new ComboBox<>(listM);
        new AutoCompleteComboBoxListener(toBox);
        toBox.setEditable(true);
        toBox.setLayoutX(231);
        toBox.setLayoutY(184);
        toBox.setPromptText("Куда");
        toBox.setFocusTraversable(false);
        toBox.setId("to-box");
        //
        datePicker = new DatePicker();
        datePicker.setLayoutX(403);
        datePicker.setLayoutY(184);
        datePicker.setOnAction(actionEvent -> {
            dayOfWeek = getDayOfWeek(datePicker);
        });
        datePicker.setPromptText("Сегодня");
        datePicker.setShowWeekNumbers(true);
        datePicker.setFocusTraversable(false);
        datePicker.setId("pick-date");
        AnchorPane root = new AnchorPane();
        root.setBackground(Background.fill(rgb(241, 242, 246)));


        Button searchButton = new Button("Доступные маршруты");
        searchButton.setLayoutX(224);
        searchButton.setLayoutY(256);
        searchButton.setStyle("-fx-font-family:Segoe UI;-fx-background-color:rgb(250,107,107);-fx-effect:none;-fx-text-fill:black;-fx-background-radius:5px;-fx-label-padding:5px;");
        searchButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            String destName;
            if (toBox.getValue() != null){
                destName = toBox.getValue();
            }
            else{
                destName = listM.get(0);
            }
            //
            AnchorPane root1 = new AnchorPane();

            ScrollPane rootScroll = new ScrollPane(root1);
            root1.setStyle("-fx-background-color:rgb(245, 245, 245)");
            Stage stage1 = new Stage();
            Scene scene1 = new Scene(rootScroll, 600, 500);
            String stylesheet = getClass().getResource("styles.css").toExternalForm();
            scene1.getStylesheets().add(stylesheet);
            stage1.setScene(scene1);
            stage1.show();
            stage.hide();

            Button backButton = new Button("\uD83E\uDC60");
            backButton.setStyle("-fx-font-weight:bold;-fx-font-family:Segoe UI;-fx-background-color:rgb(250, 107, 107);-fx-effect:none;-fx-text-fill:black;-fx-background-radius:5px;-fx-label-padding:5px;");
            root1.getChildren().add(backButton);
            backButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent mouseEvent) -> {
                stage.show();
                stage1.hide();
            });
            //
            AnchorPane paneForRouteData = new AnchorPane();
            paneForRouteData.setLayoutX(49);
            Label headLabel = new Label("Маршруты Ростов-на-Дону — " + destName);
            headLabel.setStyle("-fx-font-family:Segoe UI;-fx-font-size:19px;-fx-font-weight:bold");
            headLabel.setLayoutX(14);
            headLabel.setLayoutY(14);
            Label dateLabel = new Label(getDate(getPickedDate()));
            dateLabel.setStyle("-fx-font-family:Segoe UI;-fx-font-size:13px");
            dateLabel.setLayoutX(14);
            dateLabel.setLayoutY(38);

            paneForRouteData.getChildren().add(headLabel);
            paneForRouteData.getChildren().add(dateLabel);
            root1.getChildren().add(paneForRouteData);
            root1.setBackground(Background.fill(rgb(241, 242, 246)));
            //
            dayOfWeek = getDayOfWeek(datePicker);
            List list = mysqlconnect.getRouteByName(destName);
            List list1 = mysqlconnect.getRouteByStopName(destName);
            List<ArrayList<String>> totalList=new ArrayList();
            for(int i = 0; i < list.size(); i++){
                totalList.add(new ArrayList<>());
                totalList.get(i).addAll((Collection<? extends String>) list.get(i));
                totalList.get(i).addAll((Collection<? extends String>) list1.get(i));
            }
            List needDays = new ArrayList();
            Integer index = 0;
            AnchorPane routesPane = new AnchorPane();
            routesPane.setLayoutY(67);
            root1.getChildren().remove(routesPane);
            routesPane.getChildren().removeAll();
            for (Object o : (Collection) totalList.get(0)) {
                if (String.valueOf(o).contains(String.valueOf(dayOfWeek))){
                    needDays.add(o);
                    Integer routeId = Integer.parseInt(String.valueOf(((List)totalList.get(4)).get(index)));
                    String routeOrStopName = String.valueOf(((List)totalList.get(5)).get(index));
                    String deptime = String.valueOf(((List)totalList.get(1)).get(index));
                    String realDepTime = deptime.substring(0, deptime.length() - 3);
                    String arrtime = String.valueOf(((List)totalList.get(2)).get(index));
                    String realArrTime = arrtime.substring(0, arrtime.length() - 3);
                    Integer price = Integer.parseInt(String.valueOf(((List)totalList.get(3)).get(index)));
                    routeGUI GUI1 = new routeGUI(getPickedDate(), mysqlconnect, routeId, routeOrStopName, destName, realDepTime + " - " + realArrTime, price, stage1);
                    GUI1.getMainPane().setLayoutY(128 * index);
                    routesPane.getChildren().add(GUI1.getMainPane());
                    index++;
                }
            }
            root1.getChildren().add(routesPane);
        });
        root.getChildren().add(signIn);
        root.getChildren().add(nameLabel);
        root.getChildren().add(fromLabel);
        root.getChildren().add(txtLabel);
        root.getChildren().add(datePicker);
        root.getChildren().add(toBox);
        root.getChildren().add(searchButton);

        Scene scene = new Scene(root, 600, 500);
        String stylesheet = getClass().getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);

        stage.setScene(scene);
        stage.show();
    }
}