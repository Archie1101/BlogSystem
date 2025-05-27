module com.cczu.blogsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.cczu.blogsystem.view  to javafx.fxml;
    exports com.cczu.blogsystem.controller;
    opens com.cczu.blogsystem.controller to javafx.fxml;
}