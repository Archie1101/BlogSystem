package com.cczu.blogsystem.dao;

import com.cczu.blogsystem.pojo.BlogType;
import com.cczu.blogsystem.util.DBConnection;

import java.sql.*;

public class BlogTypeDao {
    static Connection conn = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;

    public BlogType findBlogType(String typeName) {
        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT * FROM BlogType WHERE typeName = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, typeName);
            rs = ps.executeQuery();

            if (rs.next()) {
                BlogType blogType = new BlogType();
                blogType.setTypeId(rs.getInt("typeId"));
                blogType.setTypeName(rs.getString("typeName"));
                return blogType;
            }

            sql = "INSERT INTO BlogType(typeName) VALUES(?)";
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, typeName);

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int Id = rs.getInt(1);
                BlogType blogType = new BlogType();
                blogType.setTypeId(Id);
                blogType.setTypeName(typeName);
                return blogType;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
