module com.cczu.blogsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.cczu.blogsystem.view to javafx.fxml;
    opens com.cczu.blogsystem.pojo to javafx.fxml;
    opens com.cczu.blogsystem.controller to javafx.fxml;
    exports com.cczu.blogsystem.controller;
    exports com.cczu.blogsystem.pojo;

}