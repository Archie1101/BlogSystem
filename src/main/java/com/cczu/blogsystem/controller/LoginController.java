package com.cczu.blogsystem.controller;

//业务

import com.cczu.blogsystem.dao.UserDao;
import com.cczu.blogsystem.pojo.User;

//JavaFx类
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

//Java标准库
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        UserDao userDao = new UserDao();
        User user = userDao.login("linda92", "Pwd!2345");//测试账户
        // User user = userDao.login(usernameField.getText(), passwordField.getText());

        if (user != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cczu/blogsystem/view/BlogMain.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("博客系统");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("登录错误");
            alert.setHeaderText("登录失败");
            alert.setContentText("用户名或密码错误，请重试。");
            alert.showAndWait();

        }
    }

    @FXML
    private void handleGoToRegister(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cczu/blogsystem/view/Register.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("注册");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
