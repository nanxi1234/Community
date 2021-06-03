package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.naming.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Value("${community.path.domin}")//@value注解：从配置文件里面取出值
    private String domin;
    @Value("${server.servlet.context-path}")//项目名
    private String contextPath;



    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) {//注册业务
        Map<String, Object> map = new HashMap<String, Object>();
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空!");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
        }
        //验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }
        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册！");
            return map;
        }
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));//用随机字符串的五位作为Salt
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        Date date = new Date();
        user.setCreateTime(date);
        //设置默认头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userMapper.insertUser(user);//没有问题后放入数据库
        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());//往哪发?
        //http://localhost:8080/community/activation/101/code
        String url = domin + contextPath + "/activation/" + user.getId() + "/" +user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }

        //激活有三种结果：成功，失败，重复激活
   public int activation(int userId,String code) {
       User user = userMapper.selectById(userId);
       if (user.getStatus() == 1) {
           return ACTIVATION_REPEAT;
       } else if (user.getActivationCode().equals(code)) {
           userMapper.updateStatus(userId, 1);
           return ACTIVATION_SUCCESS;
       } else {
           return ACTIVATION_FAILURE;
       }
        }
   public Map<String,Object> login(String username,String password,int expriedSencond)//登录
   {
       Map<String,Object> map =new HashMap<>();
       //验证状态
       if(StringUtils.isBlank(username)){
           map.put("usernameMsg","账号不能为空!");
           return map;
       }
       if(StringUtils.isBlank(password)){
           map.put("passwordMsg","密码不能为空");
           return map;
       }
       //不为空则验证账号
       User user = userMapper.selectByName(username);
     if(user == null){
         map.put("usernameMsg","该账号不存在");
         return map;
     }
     if(user.getStatus() == 0)//注册了，没激活
     {
         map.put("usernameMsg","该账号未激活");
         return map;
     }
     //验证密码
       password=CommunityUtil.md5(password+user.getSalt());
     if(!user.getPassword().equals(password))
     {
         map.put("passwordMsg","密码不正确");
         return map;
     }
     //生成登录凭证
       LoginTicket loginTicket=new LoginTicket();
     loginTicket.setUserId(user.getId());//使ticket中的id与user表中的id一致
     loginTicket.setTicket(CommunityUtil.generateUUID());//浏览器只需要记录key
     loginTicket.setStatus(0);
     loginTicket.setExpired(new Date(System.currentTimeMillis()+expriedSencond*1000));
     loginTicketMapper.insertLoginTicket(loginTicket);//放入数据库
       map.put("ticket",loginTicket.getTicket());//把ticket给浏览器
     return map;
   }
   public void logout(String ticket)
   {
       loginTicketMapper.updateStatus(ticket,1);//将状态改变为无效
   }
   public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
   }
}

