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
            String sql = "select * from t_user where userName = ?";
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
                String sql = "insert into t_user (userName,passWord,phone) values (?,?,?)";
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
            String sql = "select * from user where username = ? and password = ?";
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
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            String deleteCommentsSql = "DELETE FROM t_comment WHERE userId = ?";
            ps = conn.prepareStatement(deleteCommentsSql);
            ps.setInt(1, user.getUserId());
            ps.executeUpdate();
            ps.close();

            String findBlogIdsSql = "SELECT blogId FROM t_blog WHERE userId = ?";
            ps = conn.prepareStatement(findBlogIdsSql);
            ps.setInt(1, user.getUserId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                String deleteBlogCommentsSql = "DELETE FROM t_comment WHERE blogId = ?";
                PreparedStatement ps2 = conn.prepareStatement(deleteBlogCommentsSql);
                ps2.setInt(1, blogId);
                ps2.executeUpdate();
                ps2.close();
            }
            rs.close();
            ps.close();

            String deleteBlogsSql = "DELETE FROM t_blog WHERE userId = ?";
            ps = conn.prepareStatement(deleteBlogsSql);
            ps.setInt(1, user.getUserId());
            ps.executeUpdate();
            ps.close();

            String deleteUserSql = "DELETE FROM t_user WHERE userId = ?";
            ps = conn.prepareStatement(deleteUserSql);
            ps.setInt(1, user.getUserId());
            ps.executeUpdate();
            ps.close();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
