package cheng.Community.controller;

import cheng.Community.service.TestService;
import cheng.Community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/Test")
public class AlpthController {

    // ——————1.2 搭建开发环境——————
    @RequestMapping("/Hello") //页面访问路径
    @ResponseBody
    public String Hello(){
        return "Hello I am ChengJianjia!! ——20200822";
    }


    // ——————1.6 Spring入门——————
    @Autowired
    private TestService testservice;
    @RequestMapping("/data")  //浏览器注解路径
    @ResponseBody
    public String getdata(){
        return testservice.find();
    }


    // ——————1.14 Spring MVC入门——————
    @RequestMapping("/http")    //处理浏览器请求
    public void http(HttpServletRequest request, HttpServletResponse responce){
        // 获取请求数据
        System.out.println(request.getMethod());  //获取请求方法
        System.out.println(request.getServletPath());  //获取请求路径
        Enumeration<String> enumeration = request.getHeaderNames();  //得到所有请求行的key,得到一个迭代器对象
        //遍历迭代器
        while (enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name+":"+value);
        }
        System.out.println(request.getParameter("code"));  //获得请求体

        // responce给浏览器返回响应数据
        responce.setContentType("text/html;charset=utf-8");//设置返回数据类型
        try{
            PrintWriter writer = responce.getWriter();
            writer.write("<hl>手账小圈</hl>");
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    // GET请求 用于获取某些数据
    // 假设查询所有的学生   /students?current=1&limit=20   //当前第一页，最多显示20条数据
    @RequestMapping(path="/students", method = RequestMethod.GET)  //明确处理GET请求
    @ResponseBody
    public String getStudents(
            @RequestParam(name="current",required = false,defaultValue="1")int current,
            @RequestParam(name="limit",required = false,defaultValue="10")int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }
    // 查询一个学生   /students/123
    @RequestMapping(path="/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student";
    }

    // POST请求 用于提交数据
    @RequestMapping(path="/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);   //数据名称与html中一致，就能得到
        return "success";
    }


    // 向浏览器返回响应HTML数据
    @RequestMapping(path="/teacher", method = RequestMethod.GET)// 默认返回html
    public ModelAndView getTeacher(){  //返回model和view两份数据
        ModelAndView mav = new ModelAndView();
        mav.addObject("name","chengjianjia");
        mav.addObject("age","23");
        // 设置模板的路径和名字
        mav.setViewName("/demo/view");
        return mav;
    }
    // 另一种方法，建议用这种
    @RequestMapping(path="/school", method = RequestMethod.GET)
    public String getSchool(Model model){  //返回view路径
        model.addAttribute("name","北大");
        model.addAttribute("age","80");
        return "/demo/view"; //把view返回，model放到model参数里
    }

    // 向浏览器返回响应JSON数据（一般在异步请求中）
    // JAVA对象 -> JSON字符串 -> JS对象
    @RequestMapping(path="/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","程剑佳");
        emp.put("age","23");
        emp.put("salary",8000.00);
        return emp;
    }
    @RequestMapping(path="/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        List<Map<String,Object>> emps = new ArrayList<>();
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","程剑佳");
        emp.put("age","23");
        emp.put("salary",8000.00);
        emps.add(emp);

        emp.put("name","程剑佳2");
        emp.put("age","25");
        emp.put("salary",12000.00);
        emps.add(emp);
        return emps;
    }

    // ————2.11 会话管理—————
    //cookie示例
    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        // 创建cookie 每一个cookie只能存一个key，只能存字符串，只能存少量数据
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 设置cookie生效的范围
        cookie.setPath("/cheng/Test");
        // 设置cookie的生存时间 没有设置的话关闭网页就消失
        cookie.setMaxAge(60 * 10);  //秒
        // 发送cookie
        response.addCookie(cookie);
        return "set cookie";
    }

    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookie";
    }

    // session示例
    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session) {
        // 不只能字符串，能存很多数据
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");
        return "set session";
    }

    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }




}
