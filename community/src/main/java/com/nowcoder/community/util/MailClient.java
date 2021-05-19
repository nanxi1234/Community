package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component//通用的bean
public class MailClient {
    private static final Logger logger=LoggerFactory.getLogger(MailClient.class);//方便记录日志
    @Autowired
    private JavaMailSender mailSender;//核心组件，Spring容器管理，直接注入
    @Value("${spring.mail.username}")//注入Bean，以后直接拿来用
    private String from;//发件人
    public void sendMail(String to,String subject,String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());
        }
        catch (MessagingException e){
            logger.error("发送邮件失败："+e.getMessage());
        }
    }

}
