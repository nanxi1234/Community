package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
@Configuration//表明它是个配置类
public class AlphaConfig {

    @Bean//定义第三方的Bean
    public SimpleDateFormat simpleDateFormat()//将SimpleDateFormat装配到Bean中，以便以后反复用
    {
          return new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");
    }
}
