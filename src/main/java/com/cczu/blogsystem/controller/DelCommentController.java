package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.dao.BlogDao;
import com.cczu.blogsystem.dao.CommentDao;
import com.cczu.blogsystem.pojo.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DelCommentController {
    public TextField commentIdField;
    public Label statusLabel;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void handleSubmit() {
        try {
            //判断评论是否存在
            int commentId = Integer.parseInt(commentIdField.getText().trim());
            CommentDao commentDao = new CommentDao();
            if (!commentDao.checkComment(commentId)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("删除错误");
                alert.setHeaderText("删除失败");
                alert.setContentText("评论不存在！");
                alert.showAndWait();
                return;
            }

            //删除评论
            boolean deleted = commentDao.deleteComment(commentId,user.getUserId());
            Alert alert;
            if (deleted) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("删除提示");
                alert.setHeaderText("删除成功");
                alert.setContentText("该评论已成功删除！");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("删除提示");
                alert.setHeaderText("删除失败");
                alert.setContentText("没有权限删除！");
            }
            alert.showAndWait();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("输入错误");
            alert.setHeaderText("评论ID格式不正确");
            alert.setContentText("请输入有效的整数ID！");
            alert.showAndWait();
        }
    }

}
