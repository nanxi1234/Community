package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody//单独使用@Controller不加@ResponseBody：一般在用在要返回一个视图的情况，对应前后端不分离的情况
    /*@Controller +@ResponseBody返回JSON或XML
    @ResponseBody注解的作用是将Controller的方法返回的对象通过适当的转换器转换成指定的格式之后
    写入到HTTP响应(Response)对象的body中
     */
    public String sayHello() {
        return "hello Springboot.";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取请求数据
        //请求行
        System.out.println((request.getMethod()));
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();//消息头
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println((name + ":" + value));
        }
        System.out.println(request.getParameter("code"));//获取类型：code
        //返回响应数据
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(("<h1>牛客网</h1>"));
    }
    //接受请求的数据
    //怎么返回响应数据
    //GET请求

    // /students?current=1&limit=20  当前第几页，当前最多显示多少条数据
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }
    // /student/123  id称为路径的一部分
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id)
    {
        System.out.println(id);
        return "a student";
    }

    //POST请求
     @RequestMapping(path = "/student",method=RequestMethod.POST)
    @ResponseBody
    public String savaStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
     }
     //响应动态Html数据
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        //返回MODEL和VIEW,把这两个装在一个对象中
        ModelAndView mav=new ModelAndView();
        mav.addObject("name","张三");
        mav.addObject("age","30");
        mav.setViewName("/demo/view");
        return mav;
    }
    @RequestMapping(path="/school",method=RequestMethod.GET)
    public String getSchool(Model model){//一个放在Model对象中，dispatcher有这个对象的引用，一个直接返回
        model.addAttribute("name","ECUST");
        model.addAttribute("age","80");
        return "/demo/view";
    }

// 响应Json数据(异步请求)
   //java对象 -> Json字符串 ->JS对象

    @RequestMapping(path="/emp",method=RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getemp(){
        Map<String,Object> emp=new HashMap<>();
        emp.put("name","张三");
        emp.put("age","45");
        emp.put("salary","34658");
        return emp;

    }
//cookie示例
    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody//响应：返回json字符串
    public String setCookie(HttpServletResponse response){
        //创建cookie，存放在Response里面
        Cookie cookie=new Cookie("code", CommunityUtil.generateUUID());
        //设置生效的范围：在哪些路径需要cookie
        cookie.setPath("/community/alpha");
        //设置cookie的生存时间
        cookie.setMaxAge(60*10);
        response.addCookie(cookie);//放到Response的头部
        return "setCookie";
    }
    //Http是无状态的，服务器要识别他需要一些工作
   //Cookie是服务器发送到浏览器，并保存在浏览器端的一小块数据
    //浏览器下次访问该服务器时，会自动携带该数据，并将其发送给服务器
   @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
   @ResponseBody
   public String getCookie(@CookieValue("code") String code) {//@CookieValue获取cookie
        System.out.println(code);
       return "get cookie";
   }
    //Session
    //javaEE标准，用于在服务端记录客服端信息
    //数据存放在服务端更加安全，有些数据比较隐私可以放在session中，但是会增加服务端的内存压力,能不用就不用
    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session)
    {
        session.setAttribute("id",1);
        session.setAttribute("name","Test");
        return "set session";
    }
    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }
    //分布式部署时，使用session会有什么问题？
    //session不同步，解决方案，把session统一放在nosql数据库redis中
    
    }


