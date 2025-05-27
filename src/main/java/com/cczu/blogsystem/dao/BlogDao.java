package com.cczu.blogsystem.dao;

import com.cczu.blogsystem.pojo.Blog;
import com.cczu.blogsystem.pojo.BlogType;
import com.cczu.blogsystem.pojo.User;
import com.cczu.blogsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlogDao {
    static Connection conn = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;

    public boolean addBlog(Blog blog) {
        boolean result = false;
        try {
            conn = DBConnection.getConnection();
            String sql = "insert into t_blog (blogTitle,blogContent,userId,typeId) values (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, blog.getBlogTitle());
            ps.setString(2, blog.getBlogContent());
            ps.setInt(3, blog.getUser().getUserId());
            ps.setInt(4, blog.getBlogType().getTypeId());
            int i = ps.executeUpdate();
            if (i == 1) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void findAllBlogs() {
        try {
            conn = DBConnection.getConnection();
            String sql = "select t.blogId blogId,t.blogTitle blogTitle,t.blogContent blogContent,t.typeName,t_user.userName userName from(select t_blog.*,blog_type.typeName from t_blog left join blog_type on t_blog.typeId = blog_type.typeId) t left join t_user on t.userId = t_user.userId;";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("blogId\tblogTitle\tblogContent\ttypeName\tuserName");
            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                String blogTitle = rs.getString("blogTitle");
                String blogContent = rs.getString("blogContent");
                String typeName = rs.getString("typeName");
                String userName = rs.getString("userName");
                System.out.println(blogId + "\t" + blogTitle + "\t" + blogContent + "\t" + typeName + "\t" + userName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findMyBlogs(User user) {
        try {
            conn = DBConnection.getConnection();
            String sql = "select blog.*,blog_type.typeName from (select * from t_blog where userId = ?)blog left join blog_type on blog.typeId = blog_type.typeId";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();
            System.out.println("我创建的博客如下：");
            System.out.println("blogId\tblogTitle\tblogContent\ttypeName");
            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                String blogTitle = rs.getString("blogTitle");
                String blogContent = rs.getString("blogContent");
                String typeName = rs.getString("typeName");
                System.out.println(blogId + "\t" + blogTitle + "\t" + blogContent + "\t" + typeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBlog(int blogId, User user) {
        try {
            String sql;
            conn = DBConnection.getConnection();
            sql = "select * from t_blog where userId = ? and blogId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, String.valueOf(user.getUserId()));
            ps.setInt(2, blogId);
            rs = ps.executeQuery();

            if (rs.next()) {
                sql = "delete from t_comment where blogId = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, blogId);
                ps.executeUpdate();

                sql = "delete from t_blog where blogId = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, blogId);
                ps.executeUpdate();
                System.out.println("博客删除成功！");
            } else {
                System.out.println("博客删除失败！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBlog(Blog blog) {
        try {
            conn = DBConnection.getConnection();
            String sql = "update t_blog set blogTitle = ?,blogContent = ?,typeId = ? where userId = ? and blogId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, blog.getBlogTitle());
            ps.setString(2, blog.getBlogContent());
            ps.setInt(3, blog.getBlogType().getTypeId());
            ps.setInt(4, blog.getUser().getUserId());
            ps.setInt(5, blog.getBlogId());
            int i = ps.executeUpdate();
            if (i == 1) {
                System.out.println("博客修改成功！");
            } else {
                System.out.println("博客修改失败！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findBlogById(int blogId) {
        try {
            conn = DBConnection.getConnection();
            String sql = "select t.blogTitle blogTitle,t.blogContent blogContent,t.typeName,t_user.userName userName from(select blog.*,blog_type.typeName from (select * from t_blog where blogId = ?)blog left join blog_type on blog.typeId = blog_type.typeId) t left join t_user on t.userId = t_user.userId;";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, blogId);
            rs = ps.executeQuery();
            System.out.println("blogId\tblogTitle\tblogContent\ttypeName\tuserName");
            while (rs.next()) {
                String blogTitle = rs.getString("blogTitle");
                String blogContent = rs.getString("blogContent");
                String typeName = rs.getString("typeName");
                String userName = rs.getString("userName");
                System.out.println(blogId + "\t" + blogTitle + "\t" + blogContent + "\t" + typeName + "\t" + userName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findBlogByTitle(String blogTitle) {
        try {
            conn = DBConnection.getConnection();
            String sql = "select t.blogId blogId,t.blogTitle blogTitle,t.blogContent blogContent,t.typeName,t_user.userName userName from(select blog.*,blog_type.typeName from (select * from t_blog where blogTitle like ?)blog left join blog_type on blog.typeId = blog_type.typeId) t left join t_user on t.userId = t_user.userId;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + blogTitle + "%");
            rs = ps.executeQuery();
            System.out.println("blogId\tblogTitle\tblogContent\ttypeName\tuserName");
            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                String b = rs.getString("blogTitle");
                String blogContent = rs.getString("blogContent");
                String typeName = rs.getString("typeName");
                String userName = rs.getString("userName");
                System.out.println(blogId + "\t" + b + "\t" + blogContent + "\t" + typeName + "\t" + userName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkBlogById(int blogId) {
        boolean result = false;
        try {
            conn = DBConnection.getConnection();
            String sql = "select * from t_blog where blogId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, blogId);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int findUserById(int blogId) {
        try {
            conn = DBConnection.getConnection();
            String sql = "select * from t_blog where blogId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, blogId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("userId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
