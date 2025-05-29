package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.pojo.User;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddCommentController {
    public TextField blogIdField;
    public TextArea commentArea;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void handleSubmit(ActionEvent actionEvent) {
    }
}
