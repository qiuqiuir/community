package com.nowcoder.community;

import com.nowcoder.community.dao.*;
import com.nowcoder.community.entity.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @auther xurou
 * @date 2022/6/7
 */


@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void insertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150,1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150,"http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150,"654321");
        System.out.println(rows);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost>list = discussPostMapper.selectDiscussPosts(149,0,10);
        for(DiscussPost post : list){
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);

    }

    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testselectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }

    @Test
    public void testInsertDiscussPort() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(1500);
        discussPost.setTitle("Test");
        discussPost.setContent("111");
        discussPost.setType(0);
        discussPost.setStatus(0);
        discussPost.setCreateTime(new Date());
        discussPost.setCommentCount(10);
        discussPost.setScore(85.2);
        discussPostMapper.insertDiscussPost(discussPost);
    }

    @Test
    public void testSelectDiscussPostById() {
        DiscussPost post = discussPostMapper.selectDiscussPostById(280);
        System.out.println(post);
    }

    @Test
    public void testSelectCommentsByEntity() {
        List<Comment> commentList = commentMapper.selectCommentsByEntity(1, 228, 0, 100);
        for (Comment comment : commentList) {
            System.out.println(comment.toString());
        }
    }

    @Test
    public void testSelectCountByEntity() {
        int count = commentMapper.selectCountByEntity(1, 228);
        System.out.println(count);
    }

    @Test
    public void testInsertComment() {
        Comment comment = new Comment();
        comment.setUserId(111);
        comment.setEntityId(273);
        comment.setEntityType(1);
        comment.setTargetId(0);
        comment.setContent("tql!");
        comment.setCreateTime(new Date());
        commentMapper.insertComment(comment);
    }

    @Test
    public void testUpdateComment() {
        DiscussPost post = discussPostMapper.selectDiscussPostById(273);

        discussPostMapper.updateCommentCount(post.getId(), post.getCommentCount() + 1);
    }

    @Test
    public void testSelectLetters() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for(Message message : list) {
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters("111_112", 0, 20);
        for(Message message : list) {
            System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        count = messageMapper.selectLetterUnreadCount(131, null);
        System.out.println(count);

        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);
    }
}
