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
    private ListView<Blog> ListView;
    @FXML
    private Button modify;
    @FXML
    private Button delete;
    @FXML
    private Button add;
    @FXML
    private ComboBox<String> searchComboBox;
    @FXML
    private TextField searchTextField;

    private User user;

    public void setUser(User user) {
        this.user = user;
        System.out.println(user.getUserName());
        loadMyBlogs();
    }

    @FXML
    public void initialize() {
        searchComboBox.getItems().addAll("按ID搜索", "按标题搜索");
        searchComboBox.setValue("按ID搜索");
    }

    //查询自己的博客
    @FXML
    public void loadMyBlogs() {
        see();
        BlogDao blogDao = new BlogDao();
        ObservableList<Blog> blogs = FXCollections.observableArrayList(blogDao.findMyBlogs(user));
        ListView.setItems(blogs);
        ListView.setCellFactory(lv -> new ListCell<>() {
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

                    CommentDao commentDao = new CommentDao();
                    List<Comment> comments = commentDao.findCommentById(blog.getBlogId());

                    VBox commentBox = new VBox();
                    commentBox.setSpacing(5);

                    int count = 0;
                    for (Comment comment : comments) {
                        if (count >= 5) break;
                        Label commentLabel = new Label(comment.getUser().getUserName() + ":" + comment.getCommentContent());
                        commentBox.getChildren().add(commentLabel);
                        count++;
                    }
                    vbox.setSpacing(10);
                    vbox.getChildren().addAll(titleLabel, idLabel, contentLabel, commentBox);
                    setText(null);
                    setGraphic(vbox);
                }
            }
        });
        ListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // 双击打开评论界面
                Blog selectedBlog = ListView.getSelectionModel().getSelectedItem();
                if (selectedBlog != null) {
                    showCommentsWindow(selectedBlog);
                }
            }
        });
    }

    //查询自己的评论
    @FXML
    private void loadMyComments() {
        see();
        BlogDao blogDao = new BlogDao();
        ObservableList<Blog> blogs = FXCollections.observableArrayList(blogDao.findMyBlogsByComment(user));
        ListView.setItems(blogs);
        ListView.setCellFactory(lv -> new ListCell<>() {
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

                    CommentDao commentDao = new CommentDao();
                    List<Comment> comments = commentDao.findCommentById(blog.getBlogId());

                    VBox commentBox = new VBox();
                    commentBox.setSpacing(5);

                    int count = 0;
                    for (Comment comment : comments) {
                        if (count >= 5) break;
                        Label commentLabel = new Label(comment.getUser().getUserName() + ":" + comment.getCommentContent());
                        commentBox.getChildren().add(commentLabel);
                        count++;
                    }
                    vbox.setSpacing(10);
                    vbox.getChildren().addAll(titleLabel, idLabel, contentLabel, commentBox);
                    setText(null);
                    setGraphic(vbox);
                }
            }
        });
    }

    //评论展开
    @FXML
    private void showCommentsWindow(Blog blog) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cczu/blogsystem/view/Comment.fxml"));
            Parent root = fxmlLoader.load();

            CommentController controller = fxmlLoader.getController();
            controller.setBlog(blog);

            Stage stage = new Stage();
            stage.setTitle(blog.getBlogTitle() + "的全部评论");
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //搜索
    @FXML
    public void handleSearch() {
        unsee();
        String type = searchComboBox.getValue();
        String keyword = searchTextField.getText();
        //判断输入是否为空
        ObservableList<Blog> blogs;
        BlogDao blogDao = new BlogDao();
        if (type.equals("按ID搜索")) {
            if (keyword.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("输入异常");
                alert.setHeaderText("搜索失败");
                alert.setContentText("请输入搜索内容");
                alert.showAndWait();
                return;
            }
            try {
                int blogId = Integer.parseInt(keyword);
                blogs = FXCollections.observableArrayList(blogDao.findBlogById(blogId));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("输入异常");
                alert.setHeaderText("搜索失败");
                alert.setContentText("请输入有效的ID");
                alert.showAndWait();
                return;
            }
        } else {
            blogs = FXCollections.observableArrayList(blogDao.findBlogByTitle(keyword));
        }

        ListView.setItems(blogs);
        ListView.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Blog blog, boolean empty) {
                super.updateItem(blog, empty);
                if (empty || blog == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox();
                    Label titleLabel = new Label("标题: " + blog.getBlogTitle());
                    Label idLabel = new Label("ID: " + blog.getBlogId());
                    Label EditerLabel = new Label("作者: " + blog.getUser().getUserName());
                    Label contentLabel = new Label("内容: " + blog.getBlogContent());

                    CommentDao commentDao = new CommentDao();
                    List<Comment> comments = commentDao.findCommentById(blog.getBlogId());

                    VBox commentBox = new VBox();
                    commentBox.setSpacing(5);

                    int count = 0;
                    for (Comment comment : comments) {
                        if (count >= 5) break;
                        Label commentLabel = new Label(comment.getUser().getUserName() + ":" + comment.getCommentContent());
                        commentBox.getChildren().add(commentLabel);
                        count++;
                    }
                    vbox.setSpacing(10);
                    vbox.getChildren().addAll(titleLabel, idLabel, EditerLabel, contentLabel, commentBox);
                    setText(null);
                    setGraphic(vbox);
                }
            }
        });
    }

    private void handleDelete() {
        Blog selectedBlog = ListView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("登录错误");
        alert.setHeaderText("登录失败");
        alert.setContentText("用户名或密码错误，请重试。");
        alert.showAndWait();
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
    @FXML
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

    @FXML
    public void see() {
        modify.setVisible(true);
        modify.setManaged(true);
        delete.setVisible(true);
        delete.setManaged(true);
        add.setVisible(true);
        add.setManaged(true);
    }

    @FXML
    public void unsee() {
        modify.setVisible(false);
        modify.setManaged(false);
        delete.setVisible(false);
        delete.setManaged(false);
        add.setVisible(false);
        add.setManaged(false);
    }
}

