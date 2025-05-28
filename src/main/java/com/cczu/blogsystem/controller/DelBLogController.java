package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.dao.BlogDao;
import com.cczu.blogsystem.pojo.Blog;
import com.cczu.blogsystem.pojo.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DelBLogController {

    public TextField blogIdField;
    public Label statusLabel;
    private User user;

    public void setUser(User user) {
        this.user = user;
        System.out.println(user.getUserName());

    }

    //删除
    @FXML
    private void handleSubmit() {
        String blogIdText = blogIdField.getText();
        if (blogIdText == null || blogIdText.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("请输入博客ID！");
            alert.showAndWait();
            return;
        }

        try {
            int blogId = Integer.parseInt(blogIdText.trim());
            BlogDao blogDao = new BlogDao();
            boolean flag = blogDao.deleteBlog(blogId, user);

            Alert alert;
            if (flag) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("删除提示");
                alert.setHeaderText("删除成功");
                alert.setContentText("博客删除成功！");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("删除提示");
                alert.setHeaderText("删除失败");
                alert.setContentText("无法删除，博客不存在或不属于您！");
            }
            alert.showAndWait();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("输入异常");
            alert.setHeaderText("搜索失败");
            alert.setContentText("请输入有效的ID");
            alert.showAndWait();
        }

    }


}
