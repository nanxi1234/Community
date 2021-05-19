package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CommunityApplicationTests implements ApplicationContextAware {

            private ApplicationContext applicationContext;//记录容器
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
                 this.applicationContext=applicationContext;
	}
	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);
		//从容器里获取装配的Bean
		AlphaDao alphaDao=applicationContext.getBean(AlphaDao.class);//从类型中获取Bean
		System.out.println((alphaDao.select()));
		//只有一部分要Mybatis
		alphaDao=applicationContext.getBean("alphaHibernate",AlphaDao.class);//得到的object帮我转型成AlphaDao.class
		System.out.println((alphaDao.select()));

	}
	@Test
	public void testBeanManagement(){
		AlphaService alphaService=applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);//Bean只实例化一次，想要多个实例需要注解@Scope(prototype)

		//alphaService=applicationContext.getBean(AlphaService.class);
		//System.out.println(alphaService);
	}
	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat=
				applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}
	@Autowired//把注解加在属性之前
	@Qualifier("alphaHibernate")//不按默认优先级注入，直接输入想要注入的Bean的名称
	private  AlphaDao alphaDao;//给当前属性注入AlphaDao，简洁方便

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Autowired
	private AlphaService alphaService;
	@Test
	public void testDI()
	{
		System.out.println(alphaDao);
		System.out.println(simpleDateFormat);
		System.out.println(alphaService);
	}

}
