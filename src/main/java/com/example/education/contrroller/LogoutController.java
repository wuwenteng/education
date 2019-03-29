package com.example.education.contrroller;

import com.example.education.user.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author ASUS
 */
@CrossOrigin(origins = "*")
@Controller
public class LogoutController {
    @Autowired
    HttpServletRequest request;
    @GetMapping(value = "/logout")
    @ResponseBody
    public Result logout() {
        HttpSession session = request.getSession();
        if (session.getAttribute("username")!=null) {
            session.removeAttribute("username");
            System.out.println("成功退出登录");
            Result result = new Result();
            result.setCode("200");
            result.setMessage("退出成功");
            return result;
        } else {
            System.out.println("未登录停留在该页面");
            Result result = new Result();
            result.setCode("4");
            result.setMessage("未登录");
            return result;
        }
    }
}
