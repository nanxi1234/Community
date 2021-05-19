package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("prototype")//若需要多个实例
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;
    //管理Bean
    public AlphaService()//构造器
    {
        System.out.println("实例化AlphaService");
    }
    @PostConstruct//这个方法会在构造后调用
    public void init()
    {
        System.out.println("初始化AlphaService");
    }
    @PreDestroy//销毁之前调用
    public void destroy()
    {
        System.out.println("销毁AlphaService");
    }
    public String find()
    {
        return alphaDao.select();
    }
}
