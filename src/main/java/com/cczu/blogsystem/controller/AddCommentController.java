package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.dao.BlogDao;
import com.cczu.blogsystem.dao.CommentDao;
import com.cczu.blogsystem.pojo.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddCommentController {
    public TextField blogIdField;
    public TextArea commentArea;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void handleSubmit() {
        BlogDao blogDao = new BlogDao();
        CommentDao commentDao = new CommentDao();
        int blogId = Integer.parseInt(blogIdField.getText());
        boolean flag = commentDao.deleteComment(blogId, user.getUserId(), blogDao.findBlogById(blogId).getUser().getUserId());
        if (flag) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("1");
            alert.setHeaderText("<UNK>");
            alert.setContentText("<UNK>");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("2");
            alert.setHeaderText("<UNK>");
            alert.setContentText("<UNK>");
            alert.showAndWait();

        }
    }
}
