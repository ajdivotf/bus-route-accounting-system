package com.example.demo;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.JOptionPane;

public class EditRoutesController implements Initializable {


    @FXML
    private TableView<Routes> table_routes;
    @FXML
    private TableColumn<Routes, Integer> col_trip_id;

    @FXML
    private TableColumn<Routes, Integer> col_dest_id;

    @FXML
    private TableColumn<Routes, Integer> col_route_id;

    @FXML
    private TableColumn<Routes, String> col_name;

    @FXML
    private TableColumn<Routes, String> col_days;

    @FXML
    private TableColumn<Routes, String> col_deptime;

    @FXML
    private TableColumn<Routes, String> col_arrtime;

    @FXML
    private TableColumn<Routes, Integer> col_price;

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_days;

    @FXML
    private TextField txt_deptime;

    @FXML
    private TextField txt_arrtime;

    @FXML
    private TextField txt_price;

    @FXML
    private TextField filterField;


    ObservableList<Routes> listM;
    ObservableList<Routes> dataList;

    int index = -1;

    Connection conn =null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    ConnectSQL mysqlconnect = new ConnectSQL();
    private boolean checkRoute(String name, String days, String dep_time, String arr_time, String price){
        String time_pattern = "([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?";
        Pattern pattern = Pattern.compile(time_pattern);
        Matcher matcher1 = pattern.matcher(dep_time);
        Matcher matcher2 = pattern.matcher(arr_time);
        if(matcher1.matches() && matcher2.matches()){
            String days_pattern = "[1-7]|([1-7]-[1-7]-){1,3}[1-7]|([1-7]-[1-7]-){1,3}[1-7]-[1-7]";
            pattern = Pattern.compile(days_pattern);
            matcher1 = pattern.matcher(days);
            if(matcher1.matches()){
                String price_pattern = "^[1-9][0-9]+";
                pattern = Pattern.compile(price_pattern);
                matcher1 = pattern.matcher(price);
                if(matcher1.matches()){
                    String name_pattern = "[а-яА-Я]*(-[а-яА-Я]+)?(-[а-яА-Я]+)?(, [а-яА-Я]+)?(-[0-9], [а-яА-Я]+)?( [а-яА-Я]+)?( [(][а-яА-Я]+[)])?";
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
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    public void AddRoutes () throws SQLException, IOException {
        conn = mysqlconnect.ConnectDb();
        String foreign_keys = "SET FOREIGN_KEY_CHECKS = 0;";
        Integer new_route_index = 0, new_trip_index = 0, new_dest_index = 0, smth = 0;
        String dep_time = txt_deptime.getText();
        String arr_time = txt_arrtime.getText();
        String days = txt_days.getText();
        System.out.println();
        if (checkRoute(txt_name.getText(), days, dep_time, arr_time, txt_price.getText()))
        {
            Integer price = Integer.valueOf(txt_price.getText());
            Statement statement = conn.createStatement();
            String cond = "SELECT destination_id FROM destination WHERE name='" + txt_name.getText() + "';";
            String index = "";
            ResultSet resultSet = null;
            pst = conn.prepareStatement(cond);
            resultSet = statement.executeQuery("SELECT max(destination_id) AS c FROM destination;");
            while (resultSet.next()){
                smth = Integer.valueOf(resultSet.getString("c")) + 1;
            }
            new_dest_index = smth;
            ResultSet cond_rs = pst.executeQuery();
            while (cond_rs.next()) {
                index = cond_rs.getString("destination_id");
            }
            if (index.isEmpty()){
                String sql1 = "INSERT INTO destination(destination_id, name) VALUES(" + new_dest_index + ",'" + txt_name.getText() + "');";
                statement.execute(sql1);
                //
                String destPath = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\city_names copy.txt";
                String destText = "\n" + txt_name.getText();
                Files.write(Paths.get(destPath), destText.getBytes(), StandardOpenOption.APPEND);
            }
            else{
                new_dest_index = Integer.parseInt(index);
            }
            resultSet = statement.executeQuery("SELECT max(route_id) AS c FROM routes;");
            while (resultSet.next()){
                new_route_index = Integer.valueOf(resultSet.getString("c")) + 1;
            }
            resultSet = statement.executeQuery("SELECT max(trip_id) AS c FROM trips;");
            while (resultSet.next()){
                new_trip_index = Integer.valueOf(resultSet.getString("c")) + 1;
            }
            String sql2 = "INSERT INTO trips(trip_id, departure_time, arrival_time, routes_id, price) VALUES(" + new_trip_index + ", '" + dep_time + "','"
                    + arr_time + "'," + new_route_index + ",'" + price + "')";
            String sql3 = "INSERT INTO `test_schema`.`routes` (`route_id`, `from`, `to`, `days`, `time`) VALUES ('" + new_route_index + "', " +
                    "41,'" + new_dest_index + "', '" + days + "', '" + dep_time + "');";

            pst = conn.prepareStatement(foreign_keys);
            pst.execute();
            pst = conn.prepareStatement(sql2);
            pst.execute();
            pst = conn.prepareStatement(sql3);
            pst.execute();
            //
            String routesPath = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\routes copy.txt";
            String tripsPath = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\trips copy.txt";
            String routeText = "\n" + new_route_index + ",41," + new_dest_index + ",'" + days + "','" + dep_time + "'";
            String tripText = "\n" + new_trip_index + ",'" + dep_time + "','" + arr_time + "'," + new_route_index + "," + price;
            Files.write(Paths.get(routesPath), routeText.getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(tripsPath), tripText.getBytes(), StandardOpenOption.APPEND);
            //
            foreign_keys = "SET FOREIGN_KEY_CHECKS = 1;";
            pst = conn.prepareStatement(foreign_keys);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Маршрут добавлен успешно");
            UpdateTable();
            search_route();
        }
        else{
            JOptionPane.showMessageDialog(null, "Вы ввели некорректные значения");
        }
    }
    private static Integer routeId;
    private static String destName;

    public static String getDestName() {
        return destName;
    }

    public static Integer getRouteId() {
        return routeId;
    }

    @FXML
    void getSelected (MouseEvent event) throws IOException {
        index = table_routes.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        txt_name.setText(col_name.getCellData(index));
        txt_days.setText(col_days.getCellData(index));
        txt_deptime.setText(col_deptime.getCellData(index));
        txt_arrtime.setText(col_arrtime.getCellData(index));
        txt_price.setText(col_price.getCellData(index).toString());
        if (event.getClickCount() == 2 && col_route_id.getCellData(index) != null) {
            routeId = col_route_id.getCellData(index);
            destName = col_name.getCellData(index);
            Parent root5 = FXMLLoader.load(getClass().getResource("EditStops.fxml"));

            Stage stage5 = new Stage();
            Scene scene5 = new Scene(root5);
            String stylesheet = getClass().getResource("styles.css").toExternalForm();
            scene5.getStylesheets().add(stylesheet);
            stage5.setScene(scene5);
            stage5.show();
            SignInController.getStage4().hide();
            stage5.setOnCloseRequest(we -> SignInController.getStage4().show());
        }
    }
    private void editString(String sourceString, String oldLine, String newLine) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(sourceString), StandardCharsets.UTF_8));

        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).equals(oldLine)) {
                fileContent.set(i, newLine);
                break;
            }
        }

        Files.write(Path.of(sourceString), fileContent, StandardCharsets.UTF_8);
    }
    public void Edit (){
        try {
            conn = mysqlconnect.ConnectDb();
            String foreign_keys = "SET FOREIGN_KEY_CHECKS = 0;";
            Integer route_index, trip_index, dest_index = 0;
            String dep_time = txt_deptime.getText();
            String arr_time = txt_arrtime.getText();
            String days = txt_days.getText();
            Integer price = Integer.valueOf(txt_price.getText()), old_price = col_price.getCellData(index);
            dest_index = col_dest_id.getCellData(index);
            route_index = col_route_id.getCellData(index);
            trip_index = col_trip_id.getCellData(index);
            //
            String sql1 = "UPDATE destination SET name='" + txt_name.getText() + "' WHERE destination_id=" + dest_index + ";";
            String sql2 = "UPDATE trips SET departure_time='" + dep_time + "', arrival_time='" + arr_time + "', price=" + price +" WHERE trip_id=" + trip_index + ";";
            String sql3 = "UPDATE routes SET `from`=41, `to`=" + dest_index + ", `days`='" + days + "', time='"+dep_time+"' WHERE route_id=" + route_index + ";";
            pst = conn.prepareStatement(foreign_keys);
            pst.execute();
            pst = conn.prepareStatement(sql1);
            pst.execute();
            pst = conn.prepareStatement(sql2);
            pst.execute();
            pst = conn.prepareStatement(sql3);
            pst.execute();
            //
            String old_arrtime = col_arrtime.getCellData(index);
            String old_deptime = col_deptime.getCellData(index);

            String old_dest = col_name.getCellData(index);
            String new_dest = txt_name.getText();
            editString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\city_names copy.txt",
                    old_dest, new_dest);
            String old_route = col_route_id.getCellData(index) + ",41," + dest_index + ",'" + col_days.getCellData(index) + "','" + old_deptime.substring(0, old_deptime.length() - 3) + "'";
            String new_route = col_route_id.getCellData(index) + ",41," + dest_index + ",'" + days + "','" + dep_time.substring(0, dep_time.length() - 3) + "'";
            editString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\routes copy.txt",
                    old_route, new_route);
            String old_trip = col_trip_id.getCellData(index) + ",'" + old_deptime.substring(0, old_deptime.length() - 3) + "','" + old_arrtime.substring(0, old_arrtime.length() - 3) + "',"+ col_route_id.getCellData(index) + "," + col_price.getCellData(index);
            String new_trip = col_trip_id.getCellData(index) + ",'" + dep_time.substring(0, dep_time.length() - 3) + "','" + arr_time.substring(0, arr_time.length() - 3) + "',"+ col_route_id.getCellData(index) + "," + price;
            editString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\trips copy.txt",
                    old_trip, new_trip);
            //
            foreign_keys = "SET FOREIGN_KEY_CHECKS = 1;";
            pst = conn.prepareStatement(foreign_keys);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Update");
            UpdateTable();
            search_route();
            //
            if (price != Integer.parseInt(String.valueOf(old_price))  || !dep_time.equals(old_deptime)){
                Integer new_price, stopId;
                String otherDep, otherArr, old_stop, new_stop, newTotal;
                Stops stops = new Stops();
                PreparedStatement update;
                String sql4 = "SELECT stop_id, name, arrival_time, departure_time, total_time, stop_price FROM stops WHERE route_id=" + route_index;
                String sql5;
                pst = conn.prepareStatement(sql4);
                rs = pst.executeQuery();
                while (rs.next()){
                    new_price = mysqlconnect.getPrice(rs.getString("total_time"), route_index);
                    stopId = Integer.valueOf(rs.getString("stop_id"));
                    //
                    otherDep = rs.getString("departure_time");
                    otherArr = rs.getString("arrival_time");
                    newTotal = stops.countTotalTime(dep_time, otherArr);
                    old_stop = stopId + ",'" + rs.getString("name") + "'," + route_index + ",'" + otherArr.substring(0, otherArr.length() - 3)  +
                            "','" + otherDep.substring(0, otherDep.length() - 3) + "','" + newTotal.substring(0, newTotal.length() - 3) + "'," + rs.getString("stop_price");
                    new_stop = stopId + ",'" + rs.getString("name") + "'," + route_index + ",'" + otherArr.substring(0, otherArr.length() - 3)  +
                            "','" + otherDep.substring(0, otherDep.length() - 3) + "','" + newTotal.substring(0, newTotal.length() - 3) + "'," + new_price;
                    editString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\stops copy.txt",
                            old_stop, new_stop);
                    //
                    sql5 = "UPDATE stops SET stop_price=" + new_price + " WHERE stop_id=" + stopId;
                    update = conn.prepareStatement(sql5);
                    update.execute();
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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
        String cond = "SELECT COUNT(route_id) FROM destination JOIN `routes` ON `routes`.`to`=`destination`.`destination_id` WHERE name='" + txt_name.getText() + "';";
        Integer count_routes = 0;
        Integer delete_dest_index = col_dest_id.getCellData(index);
        pst = conn.prepareStatement(cond);
        ResultSet cond_rs = pst.executeQuery();
        String foreign_keys = "SET FOREIGN_KEY_CHECKS = 0;";
        pst = conn.prepareStatement(foreign_keys);
        pst.execute();
        while (cond_rs.next()) {
            count_routes = cond_rs.getInt("COUNT(route_id)");
        }
        if (count_routes <= 1){ //если такой маршрут остался один
            String sql = "DELETE FROM destination WHERE destination_id=" + delete_dest_index + ";";
            pst = conn.prepareStatement(sql);
            pst.execute();
            //
            String del_dest = txt_name.getText();
            deleteString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\city_names copy.txt",
                    "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\city_names new.txt",
                    del_dest);
        }
        String sql1 = "DELETE FROM routes WHERE route_id=" + col_route_id.getCellData(index).toString() + ";";
        String sql2 = "DELETE FROM trips WHERE trip_id=" + col_trip_id.getCellData(index).toString() + ";";
        pst = conn.prepareStatement(sql1);
        pst.execute();
        pst = conn.prepareStatement(sql2);
        pst.execute();
        foreign_keys = "SET FOREIGN_KEY_CHECKS = 1;";
        pst = conn.prepareStatement(foreign_keys);
        pst.execute();
        //
        String deptime = txt_deptime.getText();
        String arrtime = txt_arrtime.getText();
        String price = txt_price.getText();
        String del_route = col_route_id.getCellData(index) + ",41," + delete_dest_index + ",'" + txt_days.getText() + "','" + deptime.substring(0, deptime.length() - 3) + "'";
        deleteString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\routes copy.txt",
                "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\routes new.txt",
                del_route);
        String del_trip = col_trip_id.getCellData(index) + ",'" + deptime.substring(0, deptime.length() - 3) + "','" + arrtime.substring(0, arrtime.length() - 3) + "',"+ col_route_id.getCellData(index) + "," + price;
        deleteString("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\trips copy.txt",
                "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\trips new.txt",
                del_trip);
        //
        JOptionPane.showMessageDialog(null, "Маршрут удалён успешно");
        UpdateTable();
        search_route();
    }

    public void UpdateTable(){
        col_trip_id.setCellValueFactory(new PropertyValueFactory<Routes,Integer>("trip_id"));
        col_dest_id.setCellValueFactory(new PropertyValueFactory<Routes,Integer>("dest_id"));
        col_route_id.setCellValueFactory(new PropertyValueFactory<Routes,Integer>("route_id"));
        col_name.setCellValueFactory(new PropertyValueFactory<Routes,String>("name"));
        col_days.setCellValueFactory(new PropertyValueFactory<Routes,String>("days"));
        col_deptime.setCellValueFactory(new PropertyValueFactory<Routes,String>("deptime"));
        col_arrtime.setCellValueFactory(new PropertyValueFactory<Routes,String>("arrtime"));
        col_price.setCellValueFactory(new PropertyValueFactory<Routes, Integer>("price"));
        listM = mysqlconnect.getDataRoutes();
        table_routes.setItems(listM);
    }

    @FXML
    void search_route() {
        col_name.setCellValueFactory(new PropertyValueFactory<Routes,String>("name"));
        col_days.setCellValueFactory(new PropertyValueFactory<Routes,String>("days"));
        col_deptime.setCellValueFactory(new PropertyValueFactory<Routes,String>("deptime"));
        col_arrtime.setCellValueFactory(new PropertyValueFactory<Routes,String>("arrtime"));
        col_price.setCellValueFactory(new PropertyValueFactory<Routes, Integer>("price"));

        dataList = mysqlconnect.getDataRoutes();
        table_routes.setItems(dataList);
        FilteredList<Routes> filteredData = new FilteredList<>(dataList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches username
                } else if (person.getDays().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches password
                }else if (person.getArrtime().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches password
                }
                else if (String.valueOf(person.getPrice()).indexOf(lowerCaseFilter)!=-1)
                    return true;// Filter matches email

                else
                    return false; // Does not match.
            });
        });
        SortedList<Routes> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_routes.comparatorProperty());
        table_routes.setItems(sortedData);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UpdateTable();
        search_route();
    }
}