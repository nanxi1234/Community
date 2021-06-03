package com.nowcoder.community.controller.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AlphaInterceptor implements HandlerInterceptor {
    private static final Logger logger= LoggerFactory.getLogger(AlphaInterceptor.class);
    @Override//preHandle在Controller之前执行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handle)
        throws Exception{
             logger.debug("prehandle: " + handle.toString());
             return true;
    }
    @Override//postHandle在Controller之后执行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
        logger.debug("postHandle: " + handler.toString());
    }
    @Override//afterCompletion在模板引擎执行完之后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception{
        logger.debug("afterCompletion: " + handler.toString());
    }
}
