package com.cczu.blogsystem.controller;

//业务

import com.cczu.blogsystem.dao.UserDao;
import com.cczu.blogsystem.pojo.User;

//JavaFx类
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

//Java标准库
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
        UserDao userDao = new UserDao();
        User user = new User(usernameField.getText(), passwordField.getText());
        if (passwordField.getText().equals(passwordFieldConfirm.getText())) {
            if (userDao.checkUser(user.getUserName())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("注册错误");
                alert.setHeaderText("注册失败");
                alert.setContentText("用户名已存在");
                alert.showAndWait();
            } else {
                if (userDao.register(user)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("注册成功");
                    alert.setHeaderText("注册成功");
                    alert.setContentText("账号注册成功");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("注册错误");
                    alert.setHeaderText("注册失败");
                    alert.setContentText("未知错误，请联系管理员");
                    alert.showAndWait();
                }
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("注册错误");
            alert.setHeaderText("注册失败");
            alert.setContentText("两次输入密码不一致");
            alert.showAndWait();
        }
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
