package com.cczu.blogsystem.controller;

import com.cczu.blogsystem.dao.BlogDao;
import com.cczu.blogsystem.dao.BlogTypeDao;
import com.cczu.blogsystem.pojo.Blog;
import com.cczu.blogsystem.pojo.User;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UpdateBlogController {

    public TextField blogIdField;
    public TextField titleField;
    public TextArea contentArea;
    public TextField typeField;
    private User user;


    public void setUser(User user) {
        this.user = user;
    }

    public void handleUpdate() {
        try {
            int blogId = Integer.parseInt(blogIdField.getText().trim());
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            String type = typeField.getText().trim();

            if (title.isEmpty() || content.isEmpty() || type.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "请填写所有字段！");
                return;
            }

            BlogDao blogDao = new BlogDao();
            BlogTypeDao blogTypeDao = new BlogTypeDao();
            Blog blog = new Blog();
            blog.setBlogId(blogId);
            blog.setBlogTitle(title);
            blog.setBlogContent(content);
            blog.setBlogType(blogTypeDao.findBlogType(title));
            blog.setUser(user);
            boolean flag = blogDao.updateBlog(blog);
            if (flag) {
                showAlert(Alert.AlertType.INFORMATION, "博客修改成功！");
            } else {
                showAlert(Alert.AlertType.ERROR, "修改失败，该博客不属于你！");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "博客ID格式不正确！");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "修改错误：" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
