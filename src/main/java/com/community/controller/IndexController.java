package com.community.controller;


import com.community.dao.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller

public class IndexController {

    @Autowired
    HttpServletRequest request; //通过注解获取一个request
    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @RequestMapping("/getName")
    @ResponseBody
    public String getName() {
        //通过request来获取保存在session中的用户名
        User user = (User)request.getSession().getAttribute("loginUser");
        System.out.println(user.getName());
        return user.getName();
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session, SessionStatus sessionStatus){
        session.invalidate();
        sessionStatus.setComplete();
        return "redirect:/login";
    }
}