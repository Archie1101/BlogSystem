package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.dao.BlogDao;
import com.cczu.blogsystem.dao.CommentDao;
import com.cczu.blogsystem.pojo.Comment;
import com.cczu.blogsystem.pojo.User;
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
        CommentDao commentDao = new CommentDao();
        BlogDao blogDao = new BlogDao();

        int blogId;
        String commentText = commentArea.getText().trim();

        // 判断两个字段是否为空
        if (blogIdField.getText().isEmpty() || commentText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("博客ID和评论内容均不能为空！");
            alert.showAndWait();
            return;
        }

        // 判断博客ID是否为数字
        try {
            blogId = Integer.parseInt(blogIdField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("输入异常");
            alert.setHeaderText("输出错误");
            alert.setContentText("博客ID必须为数字！");
            alert.showAndWait();
            return;
        }

        // 判断博客是否存在
        boolean exists = blogDao.checkBlogById(blogId);
        if (!exists) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("博客不存在，无法评论！");
            alert.showAndWait();
            return;
        }

        // 添加评论
        Comment comment = new Comment();
        comment.setCommentContent(commentText);
        comment.setBlog(blogDao.findBlogById(blogId));
        comment.setDate(new java.sql.Timestamp(System.currentTimeMillis()));
        comment.setUser(user);

        boolean flag = commentDao.addComment(comment);
        if (flag) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("发布提示");
            alert.setHeaderText(null);
            alert.setContentText("评论发布成功！");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("发布提示");
            alert.setHeaderText(null);
            alert.setContentText("评论发布失败！");
            alert.showAndWait();
        }
    }
}
