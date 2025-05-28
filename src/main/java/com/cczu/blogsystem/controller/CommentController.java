package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.dao.UserDao;
import com.cczu.blogsystem.pojo.Blog;
import com.cczu.blogsystem.pojo.Comment;
import com.cczu.blogsystem.dao.CommentDao;

import com.cczu.blogsystem.pojo.User;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;

import java.util.List;

public class CommentController {
    @FXML
    private ListView<String> CommentView;

    private Blog blog;

    public void setBlog(Blog blog) {
        this.blog = blog;
        System.out.println(blog.getBlogTitle());
        loadComments();
    }


    public void loadComments() {
        CommentDao commentDao = new CommentDao();
        List<Comment> comments = commentDao.findCommentById(blog.getBlogId());

        ObservableList<String> items = FXCollections.observableArrayList();
        for (Comment comment : comments) {
            items.add(comment.getUser().getUserName() + ": " + comment.getCommentContent());
        }
        CommentView.setItems(items);
    }
}
