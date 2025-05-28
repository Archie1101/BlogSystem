package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.dao.BlogDao;
import com.cczu.blogsystem.dao.CommentDao;
import com.cczu.blogsystem.dao.UserDao;
import com.cczu.blogsystem.pojo.Blog;
import com.cczu.blogsystem.pojo.Comment;
import com.cczu.blogsystem.pojo.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class BlogMainController {
    @FXML
    private ListView<Blog> BlogView;   // 和FXML中对应

    private User user;

    public void setUser(User user) {
        this.user = user;
        System.out.println(user.getUserName());
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

    //登出
    @FXML
    public void Logout(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cczu/blogsystem/view/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//            获取当前stage
            Stage stage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //注销
    public void Delete(ActionEvent actionEvent) {
        UserDao userDao = new UserDao();
        userDao.deleteUser(user);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("注销提示");
        alert.setHeaderText("注销成功");
        alert.setContentText("将返回登录界面");
        alert.showAndWait();
        Logout(actionEvent);
    }
}

