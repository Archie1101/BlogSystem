package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.dao.BlogDao;
import com.cczu.blogsystem.dao.CommentDao;
import com.cczu.blogsystem.pojo.Blog;
import com.cczu.blogsystem.pojo.Comment;
import com.cczu.blogsystem.pojo.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

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

        BlogView.setCellFactory(lv -> new ListCell<Blog>() {
            @Override
            protected void updateItem(Blog blog, boolean empty) {
                super.updateItem(blog, empty);
                if (empty || blog == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox();
                    Label titleLabel = new Label("标题: " + blog.getBlogTitle());
                    Label idLabel = new Label("ID: " + blog.getBlogId());
                    Label contentLabel = new Label("内容: " + blog.getBlogContent());

                    // 获取评论数据
                    CommentDao commentDao = new CommentDao();
                    List<Comment> comments = commentDao.findCommentById(blog.getBlogId());

                    VBox commentBox = new VBox();
                    commentBox.setSpacing(5); // 可选：增加评论间距
                    for (Comment comment : comments) {
                        Label commentLabel = new Label("评论: " + comment.getCommentContent());
                        commentBox.getChildren().add(commentLabel);
                    }

                    vbox.setSpacing(10); // 增加整体垂直间距
                    vbox.getChildren().addAll(titleLabel, idLabel, contentLabel, commentBox);

                    setText(null);
                    setGraphic(vbox);
                }
            }
        });
    }

}

