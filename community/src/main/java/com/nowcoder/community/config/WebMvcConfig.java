package com.nowcoder.community.config;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.controller.interceptor.AlphaInterceptor;
import com.nowcoder.community.controller.interceptor.LoginRequiredInterceptor;
import com.nowcoder.community.controller.interceptor.LoginTicketInterceptor;
import org.aopalliance.intercept.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {//配拦截器
    @Autowired
    private AlphaInterceptor alphaInterceptor;
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;
    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;//拦截未登录就想访问敏感信息的请求
    @Override
    public void addInterceptors(InterceptorRegistry registery)
    {
       registery.addInterceptor(alphaInterceptor)
               .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")//不拦截静态资源的路
    .addPathPatterns("/register","/login");//只希望一部分请求被拦截

        registery.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");//不拦截静态资源的路
//除了静态资源，其它都要被拦截，因此不加addPathPatterns("/register","/login")
        registery.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");//不拦截静态资源的路
    }
}
