package com.cczu.blogsystem.dao;

import com.cczu.blogsystem.pojo.User;
import com.cczu.blogsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    static Connection conn = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;

    public boolean checkUser(String userName) {
        boolean result = false;
        try {
            conn = DBConnection.getConnection();
            String sql = "select * from User where userName = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("用户名已存在");
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean register(User user) {
        boolean result = false;
        boolean b = checkUser(user.getUserName());
        if (!b) {
            try {
                conn = DBConnection.getConnection();
                String sql = "insert into user (username,password,phone) values (?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, user.getUserName());
                ps.setString(2, user.getPassWord());
                ps.setString(3, user.getPhone());
                int i = ps.executeUpdate();
                if (i == 1) {
                    result = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public User login(String userName, String passWord) {
        User user = new User();
        try {
            conn = DBConnection.getConnection();
            String sql = "select * from User where userName = ? and passWord = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setString(2, passWord);
            rs = ps.executeQuery();
            if (rs.next()) {
                user.setUserId(rs.getInt("userid"));
                user.setPhone(rs.getString("phone"));
                user.setUserName(userName);
                user.setPassWord(passWord);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteUser(User user) {
//        已经在数据库中添加触发器，删除用户前自动删除博客和评论数据
        try {
            Connection conn = DBConnection.getConnection();
            String deleteUserSql = "DELETE FROM User WHERE userId = ?";
            PreparedStatement ps = conn.prepareStatement(deleteUserSql);
            ps.setInt(1, user.getUserId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findUserById(Integer userId) {

        try {
            conn = DBConnection.getConnection();
            String sql = "select * from User where userId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            User user = new User();
            if (rs.next()) {
                user.setUserId(rs.getInt("userId"));
                user.setPhone(rs.getString("phone"));
                user.setUserName(rs.getString("userName"));
                user.setPassWord(rs.getString("passWord"));
            }
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUserByName(String userName) {
        User user = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM User WHERE userName = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setPassWord(rs.getString("passWord"));
                user.setPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
