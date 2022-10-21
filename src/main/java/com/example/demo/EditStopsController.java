package com.example.demo;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

public class EditStopsController implements Initializable {

    public Integer routeIdValue = EditRoutesController.getRouteId();
    public String destName = EditRoutesController.getDestName();
    @FXML
    private TableView<Stops> table_routes;
    @FXML
    private TableColumn<Stops, Integer> col_stop_id;

    @FXML
    private TableColumn<Stops, Integer> col_route_id;

    @FXML
    private TableColumn<Stops, String> col_name;

    @FXML
    private TableColumn<Stops, String> col_deptime;

    @FXML
    private TableColumn<Stops, String> col_arrtime;

    @FXML
    private TableColumn<Stops, Integer> col_price;
    @FXML
    private TableColumn<Stops, String> col_total_time;

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_deptime;

    @FXML
    private TextField txt_arrtime;

    @FXML
    private TextField filterField;

    @FXML
    private ComboBox<String>stopsCombo;
    ObservableList<Stops> listM;
    ObservableList<Stops> dataList;

    int index = -1;

    Connection conn =null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    PreparedStatement update = null;

    ConnectSQL mysqlconnect = new ConnectSQL();
    private boolean checkStop(String name, String dep_time, String arr_time){
        String time_pattern = "([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?";
        Pattern pattern = Pattern.compile(time_pattern);
        Matcher matcher1 = pattern.matcher(dep_time);
        Matcher matcher2 = pattern.matcher(arr_time);
        if(matcher1.matches() && matcher2.matches()){
            String name_pattern = "[а-яА-ЯёЁ]*(-[а-яА-ЯёЁ]+)?(-[а-яА-ЯёЁ]+)?(, [а-яА-ЯёЁ]+)?(-[0-9], [а-яА-ЯёЁ]+)?( [а-яА-ЯёЁ]+)?( [(][а-яА-ЯёЁ]+[)])?";
            pattern = Pattern.compile(name_pattern);
            matcher1 = pattern.matcher(name);
            if(matcher1.matches()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    public void AddStop () throws SQLException, IOException {
        conn = mysqlconnect.ConnectDb();

        Integer new_stop_index = 0;
        String dep_time = txt_deptime.getText();
        String arr_time = txt_arrtime.getText();
        String name = txt_name.getText();
        Stops stops = new Stops();
        String sql = "SELECT routes.time FROM routes WHERE route_id=" + routeIdValue ;
        pst = conn.prepareStatement(sql);
        String start_time = "00:00";
        ResultSet rs = pst.executeQuery();
        while (rs.next()){
            start_time = rs.getString("time");
        }
        List list = Collections.singletonList(mysqlconnect.getStopsCountAndTotalPrice(routeIdValue)).get(0);

        Integer totalPrice = Integer.parseInt(String.valueOf(list.get(1)));
        String total_time = stops.countTotalTime(start_time, arr_time);
        if (checkStop(name, dep_time, arr_time))
        {
            String sql1 = "SELECT count(stop_id) AS c FROM stops WHERE name='" + name + "' AND route_id=" + routeIdValue;
            pst = conn.prepareStatement(sql1);
            rs = pst.executeQuery();
            Boolean isThere = false;
            while (rs.next()){
                isThere = Integer.parseInt(rs.getString("c")) > 0;
            }
            if (isThere){
                JOptionPane.showMessageDialog(null, "Такая остановка уже существует");
            }
            else{
                String sql2 = "SELECT count(stop_id) AS c FROM stops";
                pst = conn.prepareStatement(sql2);
                rs = pst.executeQuery();
                while (rs.next()){
                    new_stop_index = Integer.parseInt(rs.getString("c")) + 1;
                }
                //
                String sql3 = "INSERT INTO stops(stop_id, name, route_id, arrival_time, departure_time, total_time, stop_price) VALUES(" +
                        new_stop_index + ",'" + name + "'," + routeIdValue + ",'" + arr_time + "','" + dep_time + "','" + total_time + "'," + totalPrice + ");";
                pst = conn.prepareStatement(sql3);
                pst.execute(sql3);
                //
                Integer new_price = totalPrice, stopId;
                String old_stop, new_stop, otherDep, otherArr, otherTotal;
                String sql4 = "SELECT name, total_time, stop_id, arrival_time, departure_time, stop_price FROM stops WHERE route_id=" + routeIdValue;
                String sql5;
                pst = conn.prepareStatement(sql4);
                rs = pst.executeQuery();
                while (rs.next()){
                    new_price = mysqlconnect.getPrice(rs.getString("total_time"), routeIdValue);
                    stopId = Integer.valueOf(rs.getString("stop_id"));
                    //
                    otherDep = rs.getString("departure_time");
                    otherArr = rs.getString("arrival_time");
                    otherTotal = rs.getString("total_time");
                    old_stop = stopId + ",'" + rs.getString("name") + "'," + routeIdValue + ",'" + otherArr.substring(0, otherArr.length() - 3)  +
                            "','" + otherDep.substring(0, otherDep.length() - 3) + "','" + otherTotal.substring(0, otherTotal.length() - 3) + "'," + rs.getString("stop_price");
                    new_stop = stopId + ",'" + rs.getString("name") + "'," + routeIdValue + ",'" + otherArr.substring(0, otherArr.length() - 3)  +
                            "','" + otherDep.substring(0, otherDep.length() - 3) + "','" + otherTotal.substring(0, otherTotal.length() - 3) + "'," + new_price;
                    editString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\stops copy.txt",
                            "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\stops new.txt",
                            old_stop, new_stop);
                    //
                    sql5 = "UPDATE stops SET stop_price=" + new_price + " WHERE stop_id=" + stopId;
                    update = conn.prepareStatement(sql5);
                    update.execute();
                }
                sql5 = "UPDATE stops SET stop_price=" + new_price + " WHERE stop_id=" + new_stop_index;
                update = conn.prepareStatement(sql5);
                update.execute();
                //
                String stopsPath = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\stops copy.txt";
                String stopsText = "\n" + new_stop_index + ",'" + name + "'," + routeIdValue + ",'" + arr_time +
                        "','" + dep_time + "','" + total_time + "'," + new_price;
                Files.write(Paths.get(stopsPath), stopsText.getBytes(), StandardOpenOption.APPEND);
                //
                JOptionPane.showMessageDialog(null, "Остановка добавлена успешно");
                UpdateTable();
                search_route();
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Вы ввели некорректные значения");
        }
    }

    @FXML
    void getSelected (MouseEvent event){
        index = table_routes.getSelectionModel().getSelectedIndex();
        if (index <= -1){

            return;
        }
        String arrTime = col_arrtime.getCellData(index);
        String depTime = col_deptime.getCellData(index);
        txt_name.setText(col_name.getCellData(index));
        txt_arrtime.setText(arrTime.substring(0, arrTime.length() - 3));
        txt_deptime.setText(depTime.substring(0, depTime.length() - 3));
    }
    private void editString(String sourceString, String outputString, String oldLine, String newLine) throws IOException {
        File sourceFile = new File(sourceString);
        File outputFile = new File(outputString);
        BufferedReader reader = new BufferedReader(new FileReader(sourceFile, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.equals(oldLine)) {
                writer.write(line);
                writer.newLine();
            }
            else {
                writer.write(newLine);
                writer.newLine();
            }
        }
        reader.close();
        writer.close();
        sourceFile.delete();
        outputFile.renameTo(sourceFile);
    }
    public void Edit (){
        try {
            conn = mysqlconnect.ConnectDb();
            Stops stops = new Stops();
            Integer stop_index = col_stop_id.getCellData(index);
            String dep_time = txt_deptime.getText();
            String arr_time = txt_arrtime.getText();
            if (checkStop(txt_name.getText(), dep_time, arr_time)){
                String sql = "SELECT routes.time FROM routes WHERE route_id=" + routeIdValue;
                pst = conn.prepareStatement(sql);
                String start_time = "00:00";
                ResultSet rs = pst.executeQuery();
                while (rs.next()){
                    start_time = rs.getString("time");
                }
                String new_total_time = stops.countTotalTime(start_time, arr_time);
                Integer new_price = mysqlconnect.getPrice(new_total_time + ":00", routeIdValue);
                String sql1 = "UPDATE stops SET name='" + txt_name.getText() + "',arrival_time='" + arr_time + "',departure_time='" + dep_time + "',total_time='"+
                        new_total_time + "',stop_price=" + new_price + " WHERE stop_id=" + stop_index + ";";
                pst = conn.prepareStatement(sql1);
                pst.execute();
                //
                String old_arrtime = col_arrtime.getCellData(index);
                String old_deptime = col_deptime.getCellData(index);
                String old_total_time = String.valueOf(col_total_time.getCellData(index));

                String old_stop = stop_index + ",'" + col_name.getCellData(index) + "'," + routeIdValue + ",'" + old_arrtime.substring(0, old_arrtime.length() - 3)  +
                        "','" + old_deptime.substring(0, old_deptime.length() - 3) + "','" + old_total_time.substring(0, old_total_time.length() - 3) + "'," + col_price.getCellData(index);
                String new_stop = stop_index + ",'" + txt_name.getText() + "'," + routeIdValue + ",'" + dep_time  +
                        "','" + arr_time + "','" + new_total_time + "'," + new_price;
                editString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\stops copy.txt",
                        "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\stops new.txt",
                        old_stop, new_stop);
                //
                JOptionPane.showMessageDialog(null, "Остановки обновлены");
                UpdateTable();
                search_route();
            }
            else
                JOptionPane.showMessageDialog(null, "Введены некорректные данные");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void deleteString(String sourceString, String outputString, String outputLine) throws IOException {
        File sourceFile = new File(sourceString);
        File outputFile = new File(outputString);
        BufferedReader reader = new BufferedReader(new FileReader(sourceFile, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.equals(outputLine)) {
                writer.write(line);
                writer.newLine();
            }
        }
        reader.close();
        writer.close();
        sourceFile.delete();
        outputFile.renameTo(sourceFile);
    }
    public void Delete() throws SQLException, IOException {
        conn = mysqlconnect.ConnectDb();
        Integer delete_stop_index = col_stop_id.getCellData(index);
        String sql1 = "DELETE FROM stops WHERE stop_id=" + delete_stop_index + ";";
        pst = conn.prepareStatement(sql1);
        pst.execute();
        //
        String depTime = col_deptime.getCellData(index);
        String arrTime = col_arrtime.getCellData(index);
        String totalTime = String.valueOf(col_total_time.getCellData(index));
        String stopPrice = String.valueOf(col_price.getCellData(index));
        String del_stop = delete_stop_index + ",'" + txt_name.getText() + "'," + col_route_id.getCellData(index) + ",'" + arrTime.substring(0, arrTime.length() - 3) + "','" + depTime.substring(0, depTime.length() - 3) + "','" + totalTime.substring(0, totalTime.length() - 3) + "'," + stopPrice;
        deleteString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\stops copy.txt",
                "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\stops new.txt",
                del_stop);
        //
        String otherDep, otherArr, otherTotal, old_stop, new_stop;
        Integer new_price, stopId;
        String sql2 = "SELECT stop_id, name, arrival_time, departure_time, total_time, stop_price FROM stops WHERE route_id=" + routeIdValue;
        String sql3;
        pst = conn.prepareStatement(sql2);
        rs = pst.executeQuery();
        while (rs.next()){
            new_price = mysqlconnect.getPrice(rs.getString("total_time"), routeIdValue);
            stopId = Integer.valueOf(rs.getString("stop_id"));
            //
            otherDep = rs.getString("departure_time");
            otherArr = rs.getString("arrival_time");
            otherTotal = rs.getString("total_time");
            old_stop = stopId + ",'" + rs.getString("name") + "'," + routeIdValue + ",'" + otherArr.substring(0, otherArr.length() - 3)  +
                    "','" + otherDep.substring(0, otherDep.length() - 3) + "','" + otherTotal.substring(0, otherTotal.length() - 3) + "'," + rs.getString("stop_price");
            new_stop = stopId + ",'" + rs.getString("name") + "'," + routeIdValue + ",'" + otherArr.substring(0, otherArr.length() - 3)  +
                    "','" + otherDep.substring(0, otherDep.length() - 3) + "','" + otherTotal.substring(0, otherTotal.length() - 3) + "'," + new_price;
            editString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\stops copy.txt",
                    "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\stops new.txt",
                    old_stop, new_stop);
            //
            sql3 = "UPDATE stops SET stop_price=" + new_price + " WHERE stop_id=" + stopId;
            update = conn.prepareStatement(sql3);
            update.execute();
        }
        //
        JOptionPane.showMessageDialog(null, "Остановка успешно удалена");
        UpdateTable();
        search_route();
    }

    public void UpdateTable(){
        col_stop_id.setCellValueFactory(new PropertyValueFactory<Stops,Integer>("stopsId"));
        col_route_id.setCellValueFactory(new PropertyValueFactory<Stops,Integer>("routeId"));
        col_name.setCellValueFactory(new PropertyValueFactory<Stops,String>("stopName"));
        col_deptime.setCellValueFactory(new PropertyValueFactory<Stops,String>("departureTime"));
        col_arrtime.setCellValueFactory(new PropertyValueFactory<Stops,String>("arrivalTime"));
        col_price.setCellValueFactory(new PropertyValueFactory<Stops, Integer>("price"));
        col_total_time.setCellValueFactory(new PropertyValueFactory<Stops, String>("totalTime"));
        listM = mysqlconnect.getStopsListByRoute(routeIdValue);
        table_routes.setItems(listM);
    }
    @FXML
    void search_route() {
        /*col_name.setCellValueFactory(new PropertyValueFactory<Stops,String>("stopName"));
        col_deptime.setCellValueFactory(new PropertyValueFactory<Stops,String>("departureTime"));
        col_arrtime.setCellValueFactory(new PropertyValueFactory<Stops,String>("arrivalTime"));
        col_price.setCellValueFactory(new PropertyValueFactory<Stops, Integer>("price"));

        dataList = mysqlconnect.getStopsListByRoute(1);
        table_routes.setItems(dataList);
        FilteredList<Stops> filteredData = new FilteredList<>(dataList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getStopName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true;
                }
                else
                    return false; // Does not match.
            });
        });
        SortedList<Stops> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_routes.comparatorProperty());
        table_routes.setItems(sortedData);*/
    }
    private void setComboBox(){
        ObservableList<String> priceList = FXCollections.observableArrayList();
        stopsCombo.setId("stopsCombo");
        stopsCombo.setOnAction(actionEvent -> {
            txt_name.setText(stopsCombo.getSelectionModel().getSelectedItem());
        });
        conn = mysqlconnect.ConnectDb();
        String sql1 = "SELECT DISTINCT stops.name, stop_price FROM destination JOIN `routes` ON `routes`.`to`=`destination`.`destination_id` INNER JOIN `stops` ON `routes`.`route_id`=`stops`.`route_id` WHERE destination.name = '" + destName + "' ORDER BY stops.name;";
        try {
            pst = conn.prepareStatement(sql1);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                priceList.add(rs.getString("stop_price"));
                stopsCombo.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    Label main_label;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        main_label.setText("Ростов-на-Дону — " + destName);
        UpdateTable();
        setComboBox();
        //search_route();
    }
}