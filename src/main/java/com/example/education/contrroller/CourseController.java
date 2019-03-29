package com.example.education.contrroller;

import com.example.education.service.*;
import com.example.education.user.Course;
import com.example.education.user.Result;
import com.example.education.user.Teacher;
import com.example.education.user.User;
import org.hibernate.mapping.ValueVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ASUS
 */

@Controller
@RequestMapping(value = "/haveLogin")
public class CourseController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    CourseService service;
    @Autowired
    TeacherService teacherService;
    @Autowired
    UserService userService;
    @Autowired
    GradeService gradeService;
    @Autowired
    StudentCourseService studentCourseService;

    /**
     * 删除课程，已成功,老师有权利删除课程么？还是只有教务人员有权限？ --两者都有权限
     * 应该只有manager有权利
    */
    @DeleteMapping(value = "/deleteCourse")
    @ResponseBody
    public Result delete(@RequestParam String number) {
        HttpSession session = request.getSession();
        Result result = new Result();
        String role = session.getAttribute("role").toString();
        if ("teacher".equals(role) || "manager".equals(role)) {
            Course course = service.select(number);
            // 删除选课记录
            studentCourseService.deleteByCourse(course);

            boolean exists = course == null;
            if (exists) {
                //该项不存在，请刷新
                System.out.println("该项不存在，请刷新");
                result.setCode("12");
                result.setMessage("该项不存在，请刷新重试");
            } else {
                service.delete(course);
                result.setCode("200");
                result.setMessage("删除成功！");
                System.out.println("删除成功");
            }
        } else {
            //没有权限
            result.setCode("16");
            result.setMessage("没有权限");
        }
        return result;
    }

    @GetMapping(value = "/selectCourse")
    @ResponseBody
    public Result selectAll() {
        HttpSession session = request.getSession();
        Result result = new Result();

        String username = session.getAttribute("username").toString();
        String role = session.getAttribute("role").toString();
        User user = userService.findByNameAndRole(username, role).get(0);
        List<Course> courses = service.selectAll();
        System.out.println(courses.size());
        result.setData(courses);
        result.setCode("200");
        result.setMessage("搜索成功");
        result.setUser(user);

        return result;
    }
}
