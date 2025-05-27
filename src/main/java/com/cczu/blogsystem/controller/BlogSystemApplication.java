package com.cczu.blogsystem.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BlogSystemApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cczu/blogsystem/view/Login.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cczu/blogsystem/view/Register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("博客系统");
        stage.setScene(scene);
        stage.setResizable(false);//禁止缩放
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}