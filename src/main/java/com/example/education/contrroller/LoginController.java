package com.example.education.contrroller;

import com.example.education.service.UserService;
import com.example.education.user.Result;
import com.example.education.user.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ASUS
 */
@CrossOrigin(origins = "*")
@Controller
public class LoginController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    UserService userService;

    /**
     * 登录
     *
     * @param user 登录人信息
     * @return 是否登录成功
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public Result login(@RequestBody User user) {
        System.out.println("start login");
        System.out.println(user.getUsername());
        if (isAuthticated(user)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role",user.getRole());
            System.out.println(session.getAttribute("username"));
            System.out.println(session.getAttribute("role"));
            User user1 = new User();
            user1.setUsername(user.getUsername());
            user1.setPassword(user.getPassword());
            user1.setRole(user.getRole());
            List<User> users = new ArrayList<>();
            users.add(user1);
            Result result = new Result();
            result.setCode("200");
            result.setMessage("登录成功");
            result.setData(users);
            System.out.println("登陆成功！");
            return result;
        } else {
            System.out.println("登陆失败！");
            Result result = new Result();
            result.setCode("2");
            result.setMessage("登录失败");
            return result;
            //response.sendRedirect("login.html");
        }
    }

    /**
     * 判断是否保存了该条信息
     * @param user 登录人信息
     * @return 登录信息是否符合记录
     */
    private boolean isAuthticated(@NotNull User user) {
        List<User> userList = userService.findByNameAndRole(user.getUsername(),user.getRole());
        int status = 0;
        if (userList != null) {
            for (User user1 : userList) {
                if (user1.getPassword().equals(user.getPassword())) {
                    status = 1;
                }
            }
        }
        if (status == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 注册
     *
     * @param user 注册信息
     * @return 是否注册成功
     */
    @PostMapping(value = "/registered")
    @ResponseBody
    public Result register(@RequestBody User user) {
        System.out.println("register");
        HttpSession session = request.getSession();
        Result result = new Result();
        boolean flag = true;
        List<User> list = userService.findByNameAndRole(user.getUsername(),user.getRole());
        if (list != null){
            for (User user1 : list) {
                String password1 = user1.getPassword();
                if (password1.equals(user.getPassword())) {
                    flag = false;
                }
            }
        }
        if (flag) {
            userService.save(user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role",user.getRole());
            System.out.println(session.getAttribute("username"));
            List<User> userList = new ArrayList<>();
            userList.add(user);
            result.setCode("202");
            result.setMessage("注册成功");
            result.setData(userList);
            return result;
        } else {
            result.setCode("6");
            result.setMessage("已经注册过");
            return result;
        }
    }

    @PostMapping(value = "/forgetPassword")
    @ResponseBody
    public Result forgetPassword(@RequestBody User user) {
        System.out.println("forgetPassword");

        Result result = new Result();
        List<User> users = userService.findByNameAndRole(user.getUsername(),user.getRole());
        boolean flag = users == null;
        if (flag) {
            result.setCode("7");
            result.setMessage("尚未注册");
        } else {
            User user1 = users.get(0);
            user1.setPassword(user.getPassword());
            userService.updatePassword(user1);
            System.out.println("更新密码成功");

            result.setCode("201");

        }
        return result;
    }
}
