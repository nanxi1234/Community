package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @ResponseBody
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


}
