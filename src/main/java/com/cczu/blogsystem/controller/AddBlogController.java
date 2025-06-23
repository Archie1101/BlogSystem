package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.dao.BlogDao;
import com.cczu.blogsystem.dao.BlogTypeDao;
import com.cczu.blogsystem.pojo.Blog;
import com.cczu.blogsystem.pojo.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddBlogController {
    @FXML
    TextField titleField;
    @FXML
    TextArea contentArea;
    @FXML
    TextField typeField;

    private User user;
    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void handleSubmit() {
        BlogDao blogDao = new BlogDao();
        BlogTypeDao blogTypeDao = new BlogTypeDao();

        Blog blog = new Blog();
        blog.setBlogTitle(titleField.getText());
        blog.setBlogContent(contentArea.getText());
        blog.setUser(user);
        blog.setBlogType(blogTypeDao.findBlogType(typeField.getText()));

        boolean flag = blogDao.addBlog(blog);

        Alert alert;
        if (flag) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("创建提示");
            alert.setHeaderText("成功");
            alert.setContentText("创建博客成功");

        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("创建提示");
            alert.setHeaderText("失败");
            alert.setContentText("创建博客失败");
        }
        alert.showAndWait();
    }
}
