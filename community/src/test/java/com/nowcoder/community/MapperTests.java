package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;


@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
@Autowired
private LoginTicketMapper loginTicketMapper;
    @Test
    public void testSelectUser(){
        User user=userMapper.selectById(101);
        System.out.println(user);

        user=userMapper.selectByName("liubei");
        System.out.println(user);

        user=userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }
    @Test
    public void testSelectPosts(){
        List<DiscussPost> list =discussPostMapper.selectDiscussPosts(0,0,10);//不按ID来查，查第一页（行号从0开始）的十条数据
        for(DiscussPost post : list)
        {
            System.out.println(post);
        }
    }
@Test
    public void testInsertLoginTicket(){
    LoginTicket loginTicket=new LoginTicket();
    loginTicket.setUserId(101);
    loginTicket.setTicket("abc");
    loginTicket.setStatus(0);
    loginTicket.setExpired(new Date(System.currentTimeMillis()));

    loginTicketMapper.insertLoginTicket(loginTicket);
}
@Test
    public void testSelectLoginTicket(){
        LoginTicket loginTicket=loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc",1);
        loginTicket=loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
}
}
