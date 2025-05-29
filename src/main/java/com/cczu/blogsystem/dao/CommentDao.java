package com.cczu.blogsystem.dao;

import com.cczu.blogsystem.pojo.Blog;
import com.cczu.blogsystem.pojo.Comment;
import com.cczu.blogsystem.pojo.User;
import com.cczu.blogsystem.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    static Connection conn = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;

    public boolean addComment(Comment comment) {
        try {
            conn = DBConnection.getConnection();
            String sql = "insert into Comment (commentContent,blogId,createTime,userId) values (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, comment.getCommentContent());
            ps.setInt(2, comment.getBlog().getBlogId());
            ps.setTimestamp(3, new Timestamp(comment.getDate().getTime()));
            ps.setInt(4, comment.getUser().getUserId());
            int i = ps.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void findMyComments(User user) {
        try {
            conn = DBConnection.getConnection();
            String sql = "select Blog.blogId,Blog.blogTitle, blog.blogContent ,comment.commentContent " +
                    "from (select * from Comment where userId = ?)comment left join  blog on comment.blogId =  blog.blogId";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();
            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                String blogTitle = rs.getString("blogTitle");
                String blogContent = rs.getString("blogContent");
                String commentContent = rs.getString("commentContent");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Comment> findCommentById(int blogId) {
        List<Comment> comments = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT t.blogId, t.blogTitle, t.blogContent, t.commentContent, user.userName " +
                    "FROM (SELECT blog.blogId, blog.blogTitle, blog.blogContent, comment.commentContent, comment.userId " +
                    "      FROM comment LEFT JOIN blog ON comment.blogId = blog.blogId " +
                    "      WHERE comment.blogId = ?) t " +
                    "LEFT JOIN user ON t.userId = user.userId";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, blogId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Comment comment = new Comment();

                comment.setCommentContent(rs.getString("commentContent"));//评论内容

                BlogDao blogDao = new BlogDao();
                Blog blog = blogDao.findBlogById(blogId);//博客
                comment.setBlog(blog);

                UserDao userDao = new UserDao();
                User user = userDao.findUserByName(rs.getString("userName"));//评论用户
                comment.setUser(user);
                comments.add(comment);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public boolean checkComment(int commentId) {
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM comment WHERE commentId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, commentId);
            rs = ps.executeQuery();
            return rs.next();  // 如果查到结果说明评论存在
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean deleteComment(int commentId, int userId) {
        try {
            conn = DBConnection.getConnection();
            String checkSql = "SELECT * FROM comment WHERE commentId = ? AND userId = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, commentId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String deleteSql = "DELETE FROM comment WHERE commentId = ?";
            ps = conn.prepareStatement(deleteSql);
            ps.setInt(1, commentId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
