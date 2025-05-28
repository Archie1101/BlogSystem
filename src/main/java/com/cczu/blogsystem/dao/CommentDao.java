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

    public void addComment(Comment comment) {
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
                System.out.println("评论发布成功！");
            } else {
                System.out.println("评论发布失败！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findMyComments(User user) {
        try {
            conn = DBConnection.getConnection();
            String sql = "select Blog.blogId blogId,Blog.blogTitle blogTitle, blog.blogContent blogContent,comment.commentContent " +
                    "from (select * from Comment where userId = ?)comment left join  blog on comment.blogId =  blog.blogId";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();
            System.out.println("我发布的评论如下：");
            System.out.println("blogId\tblogTitle\tblogContent\tcommentContent");
            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                String blogTitle = rs.getString("blogTitle");
                String blogContent = rs.getString("blogContent");
                String commentContent = rs.getString("commentContent");
                System.out.println(blogId + "\t" + blogTitle + "\t" + blogContent + "\t" + commentContent);
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


    public void deleteComment(int commentId, int userId, int createUserId) {
        try {
            conn = DBConnection.getConnection();
            String sql = "delete from  comment where commentId = ? and (userId = ? or ? = ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, commentId);
            ps.setInt(2, userId);
            ps.setInt(3, createUserId);
            ps.setInt(4, userId);
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("评论删除成功！");
            } else {
                System.out.println("评论删除失败！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
