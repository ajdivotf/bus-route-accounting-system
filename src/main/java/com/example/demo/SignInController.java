package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {
    @FXML
    PasswordField password_field;
    @FXML
    TextField username_field;
    private static Stage stage4;

    public static Stage getStage4() {
        return stage4;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage4 = new Stage();
    }
    public void SignIn(ActionEvent actionEvent) throws IOException {
        if (password_field.getText().equals("sds2013") && username_field.getText().equals("admin")){
            Parent root = FXMLLoader.load(getClass().getResource("EditRoutes.fxml"));
            Scene scene4 = new Scene(root);
            String stylesheet = getClass().getResource("styles.css").toExternalForm();
            scene4.getStylesheets().add(stylesheet);
            stage4.setScene(scene4);
            stage4.show();
            Main.getSignInStage().hide();
            //
            stage4.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    Main.getMainStage().show();
                }
            });
        }
        else{
            Main.getMainStage().show();
            Main.getSignInStage().hide();
        }
    }
}
