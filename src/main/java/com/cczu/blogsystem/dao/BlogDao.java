package com.cczu.blogsystem.dao;

import com.cczu.blogsystem.pojo.Blog;
import com.cczu.blogsystem.pojo.BlogType;
import com.cczu.blogsystem.pojo.User;
import com.cczu.blogsystem.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlogDao {
    static Connection conn = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;

    public boolean addBlog(Blog blog) {
        boolean result = false;
        try {
            conn = DBConnection.getConnection();
            String sql = "insert into Blog (blogTitle,blogContent,userId,typeId) values (?,?,?,?)";
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

    public List<Blog> findAllBlogs() {
        try {
            conn = DBConnection.getConnection();
            String sql = "select t.blogId ,t.blogTitle ,t.blogContent ,t.typeName,User.userName  from(select Blog.*,BlogType.typeName from Blog left join BlogType on Blog.typeId = BlogType.typeId) t left join User on t.userId = User.userId;";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            List<Blog> blogs = new ArrayList<>();
            while (rs.next()) {
                Blog blog = new Blog();
                blog.setBlogId(rs.getInt("blogId"));
                blog.setBlogTitle(rs.getString("blogTitle"));
                blog.setBlogContent(rs.getString("blogContent"));
                blog.setBlogContent(rs.getString("typeName"));
                blog.setBlogContent(rs.getString("userName"));
                blogs.add(blog);
            }
            return blogs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Blog> findMyBlogs(User user) {
        try {
            conn = DBConnection.getConnection();
            String sql = "select blog.*,BlogType.typeName from (select * from Blog where userId = ?)blog left join BlogType on blog.typeId = BlogType.typeId";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();
            List<Blog> blogs = new ArrayList<>();
            while (rs.next()) {
                Blog blog = new Blog();
                BlogType blogType = new BlogType();

                blog.setBlogId(rs.getInt("blogId"));
                blog.setBlogTitle(rs.getString("blogTitle"));
                blog.setBlogContent(rs.getString("blogContent"));
                blog.setUser(user);

                blogType.setTypeId(rs.getInt("typeId"));
                blogType.setTypeName(rs.getString("typeName"));
                blog.setBlogType(blogType);

                blogs.add(blog);
            }
            return blogs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Blog> findMyBlogsByComment(User user) {
        try {
            conn = DBConnection.getConnection();
            String sql = "select b.*, bt.typeName from Blog b left join BlogType bt on b.typeId = bt.typeId where b.blogId in (select distinct c.blogId from Comment c where c.userId = ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();
            List<Blog> blogs = new ArrayList<>();
            while (rs.next()) {
                Blog blog = new Blog();
                BlogType blogType = new BlogType();
                UserDao userDao = new UserDao();

                blog.setBlogId(rs.getInt("blogId"));
                blog.setBlogTitle(rs.getString("blogTitle"));
                blog.setBlogContent(rs.getString("blogContent"));
                blog.setUser(userDao.findUserById(rs.getInt("userId")));

                blogType.setTypeId(rs.getInt("typeId"));
                blogType.setTypeName(rs.getString("typeName"));
                blog.setBlogType(blogType);

                blogs.add(blog);
            }
            return blogs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteBlog(int blogId, User user) {
        try {
            String sql;
            conn = DBConnection.getConnection();
            sql = "select * from Blog where userId = ? and blogId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, String.valueOf(user.getUserId()));
            ps.setInt(2, blogId);
            rs = ps.executeQuery();

            if (rs.next()) {
                sql = "delete from Comment where blogId = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, blogId);
                ps.executeUpdate();

                sql = "delete from Blog where blogId = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, blogId);
                ps.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void updateBlog(Blog blog) {
        try {
            conn = DBConnection.getConnection();
            String sql = "update Blog set blogTitle = ?,blogContent = ?,typeId = ? where userId = ? and blogId = ?";
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

    public Blog findBlogById(int blogId) {
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT t.blogTitle, t.blogContent, t.typeName, User.userName " +
                    "FROM (SELECT blog.*, BlogType.typeName " +
                    "      FROM Blog LEFT JOIN BlogType ON blog.typeId = BlogType.typeId " +
                    "      WHERE blog.blogId = ?) t " +
                    "LEFT JOIN User ON t.userId = User.userId;";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, blogId);
            rs = ps.executeQuery();
            Blog blog = new Blog();

            if (rs.next()) {
                blog.setBlogId(blogId);//博客Id
                blog.setBlogTitle(rs.getString("blogTitle"));//博客标题
                blog.setBlogContent(rs.getString("blogContent"));//博客内容

                UserDao userDao = new UserDao();
                User user = userDao.findUserByName(rs.getString("userName"));//用户
                blog.setUser(user);

                BlogTypeDao blogTypeDao = new BlogTypeDao();
                BlogType blogType = blogTypeDao.findBlogType(rs.getString("typeName"));//博客类型
                blog.setBlogType(blogType);
                return blog;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Blog findBlogByTitle(String blogTitle) {
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT t.blogId, t.blogTitle, t.blogContent, t.typeName, User.userName " +
                    "FROM (SELECT blog.*, BlogType.typeName " +
                    "      FROM Blog LEFT JOIN BlogType ON blog.typeId = BlogType.typeId " +
                    "      WHERE blog.blogTitle LIKE ?) t " +
                    "LEFT JOIN User ON t.userId = User.userId;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + blogTitle + "%");
            rs = ps.executeQuery();

            if (rs.next()) {
                Blog blog = new Blog();
                blog.setBlogId(rs.getInt("blogId"));
                blog.setBlogTitle(rs.getString("blogTitle"));
                blog.setBlogContent(rs.getString("blogContent"));

                UserDao userDao = new UserDao();
                User user = userDao.findUserByName(rs.getString("userName"));
                blog.setUser(user);

                BlogTypeDao blogTypeDao = new BlogTypeDao();
                BlogType blogType = blogTypeDao.findBlogType(rs.getString("typeName"));
                blog.setBlogType(blogType);

                return blog;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkBlogById(int blogId) {
        boolean result = false;
        try {
            conn = DBConnection.getConnection();
            String sql = "select * from Blog where blogId = ?";
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
            String sql = "select * from Blog where blogId = ?";
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
