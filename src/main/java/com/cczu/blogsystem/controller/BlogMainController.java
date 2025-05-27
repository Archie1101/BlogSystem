package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.dao.BlogDao;
import com.cczu.blogsystem.pojo.Blog;
import com.cczu.blogsystem.pojo.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class BlogMainController {
    @FXML
    private ListView<Blog> BlogView;   // 和FXML中对应

    private User user;

    public void setUser(User user) {
        this.user = user;
        System.out.println("收到用户：" + user.getUserName());
        loadBlogs();
    }

    public void loadBlogs() {
        BlogDao blogDao = new BlogDao();
        ObservableList<Blog> blogs = FXCollections.observableArrayList(blogDao.findMyBlogs(user));
        BlogView.setItems(blogs);

        // 如果需要自定义列表显示，可以加cellFactory
        BlogView.setCellFactory(lv -> new ListCell<Blog>() {
            @Override
            protected void updateItem(Blog blog, boolean empty) {
                super.updateItem(blog, empty);
                if (empty || blog == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(blog.getBlogTitle());  // 简单显示标题
                }
            }
        });
    }
}

