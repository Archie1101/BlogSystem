package com.cczu.blogsystem.dao;

import com.cczu.blogsystem.pojo.BlogType;
import com.cczu.blogsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlogTypeDao {
    static Connection conn = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;

    public BlogType findBlogType(String typeName) {
        try {
            conn = DBConnection.getConnection();
            String sql = "select * from BlogType where typeName=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, typeName);
            rs = ps.executeQuery();
            if (rs.next()) {
                BlogType blogType = new BlogType();
                blogType.setTypeId(rs.getInt("typeId"));
                blogType.setTypeName(rs.getString("typeName"));
                return blogType;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
