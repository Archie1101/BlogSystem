package com.cczu.blogsystem.util;

import java.sql.*;

public class DBConnection {

    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/BlogSystem?characterEncoding=UTF-8&useSSL=false";
    private static final String username = "root";
    private static final String password = "123123";
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return conn;
        }
        return conn;
    }

    public static void close(ResultSet rs, PreparedStatement ps, Connection connection) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
