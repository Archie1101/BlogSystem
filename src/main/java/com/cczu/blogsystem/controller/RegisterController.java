package com.cczu.blogsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordFieldConfirm;

    @FXML
    private void handleRegister(ActionEvent event) {

    }

    @FXML
    private void handleGoToLogin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cczu/blogsystem/view/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // 获取当前窗口
            stage.setScene(scene);
            stage.setTitle("登录");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
