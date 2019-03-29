package com.example.education.contrroller;

import com.example.education.service.ManagerService;
import com.example.education.service.StudentService;
import com.example.education.service.TeacherService;
import com.example.education.service.UserService;
import com.example.education.user.*;
import com.example.education.util.VerificationCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ASUS
 */
@CrossOrigin(origins = "*")
@Controller
public class RouterController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    TeacherService teacherService;
    @Autowired
    StudentService studentService;
    @Autowired
    ManagerService managerService;
    @Autowired
    UserService userService;

    @GetMapping(value = "/login")
    public String login() {
        System.out.println("login文件");
        return "redirect:/login.html";
    }

    @GetMapping(value = "/")
    public String index() {
        System.out.println("login文件");
        return "redirect:/login.html";
    }

    @GetMapping(value = "/haveLogin/getUsername")
    @ResponseBody
    public Result getUsername() {
        HttpSession session = request.getSession();
        Result result = new Result();
        String username = session.getAttribute("username").toString();
        String role = session.getAttribute("role").toString();
        User user = userService.findByNameAndRole(username,role).get(0);
        if ("teacher".equals(role)) {
            Teacher teacher = teacherService.select(username).get(0);
            // 返回number,显示在个人信息一栏
            user.setPassword(teacher.getNumber());
            result.setUser(user);
            result.setMessage("成功返回Username");
            result.setCode("200");
        }else if ("student".equals(role)){
            Student student = studentService.select(username).get(0);

            // 返回number,显示在个人信息一栏
            user.setPassword(student.getNumber());
            result.setUser(user);
            result.setMessage("成功返回Username");
            result.setCode("200");
        } else if ("manager".equals(role)) {
            Manager manager = managerService.selectByNum(username);

            //返回number,显示在个人信息一栏
            user.setPassword(manager.getName());
            result.setUser(user);
            result.setMessage("成功返回Username");
            result.setCode("200");
        } else {
            result.setUser(null);
            result.setMessage("未登录");
            result.setCode("01");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/saveUser")
    @ResponseBody
    public Result saveUser() {
        HttpSession session = request.getSession();
        Result result = new Result();
        String role = session.getAttribute("role").toString();
        String username = request.getParameter("username");
        System.out.println(username);

        String password = request.getParameter("password");
        System.out.println(password);

        String userRole = request.getParameter("role");
        System.out.println(userRole);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(userRole);
        if ("manager".equals(role)) {
            userService.save(user);
            result.setCode("200");
            result.setMessage("保存用户成功");
        } else {
            result.setCode("2");
            result.setMessage("保存用户失败，您还未登录或不是教务人员");
        }
        return result;
    }

    @GetMapping(value = "/getCode")
    @ResponseBody
    public void getCode(HttpServletResponse response) {

        // 调用工具类生成验证码和验证码图片
        VerificationCodeUtil util = new VerificationCodeUtil();
        Map<String,Object> codeMap = util.generateCodeAndPic();
        System.out.println(codeMap.get("code").toString());

        // 将四位数字的验证码保存到session中
        HttpSession session = request.getSession();
        session.setAttribute("code",codeMap.get("code").toString());

        //禁止图像缓存
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", -1);

        response.setContentType("image/jpeg");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos;
        try {
            sos = response.getOutputStream();
            ImageIO.write((RenderedImage) codeMap.get("codePic"), "jpeg", sos);
            sos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/getValidationCode")
    @ResponseBody
    public Result getValidation() {
        Result result = new Result();

        System.out.println("检验验证码");
        String validationCode = request.getParameter("validation");
        String codeInput = validationCode.toLowerCase();
        String validation = request.getSession().getAttribute("code").toString();
        String codeOriginal = validation.toLowerCase();
        if (codeInput.equals(codeOriginal)) {
            result.setCode("200");
        } else {
            result.setCode("400");
            result.setMessage("验证码错误");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/changePassword")
    @ResponseBody
    public Result changePassword() {
        Result result = new Result();
        String newPassword = request.getParameter("newPassword");
        String oldPassword = request.getParameter("oldPassword");
        String username = request.getParameter("username");
        String role = request.getParameter("role");
        User user = userService.findByNameAndRole(username, role).get(0);
        if (user.getPassword().equals(oldPassword)) {

            user.setPassword(newPassword);
            userService.updatePassword(user);
            result.setCode("200");
        } else {
            System.out.println("原密码错误");
            result.setCode("9");
            result.setMessage("原密码错误");
        }
        return result;
    }
}
