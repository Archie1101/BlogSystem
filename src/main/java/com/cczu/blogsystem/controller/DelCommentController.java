package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.pojo.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DelCommentController {
    public TextField commentIdField;
    public Label statusLabel;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void handleSubmit(ActionEvent actionEvent) {
    }
}
