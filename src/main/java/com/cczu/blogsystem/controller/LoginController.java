package com.cczu.blogsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.cczu.blogsystem.util.DBConnection;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "select * from user where user_name = ? and password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, usernameField.getText());
            ps.setString(2, passwordField.getText());
            System.out.println(ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("点击了登录按钮");
                System.out.println("用户名：" + usernameField.getText());
                System.out.println("密码：" + passwordField.getText());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToRegister(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cczu/blogsystem/view/Register.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // 当前窗口
            stage.setScene(scene);
            stage.setTitle("注册");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
