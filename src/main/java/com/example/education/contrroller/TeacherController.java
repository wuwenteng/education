package com.example.education.contrroller;

import com.example.education.service.GradeService;
import com.example.education.service.StudentCourseService;
import com.example.education.service.TeacherService;
import com.example.education.user.Course;
import com.example.education.user.Grade;
import com.example.education.user.Result;
import com.example.education.user.Teacher;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ASUS
 */
@CrossOrigin(origins = "*")
@Controller
public class TeacherController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    TeacherService service;
    @Autowired
    GradeService gradeService;
    @Autowired
    StudentCourseService studentCourseService;

    @PostMapping(value = "/haveLogin/saveTeacher")   //@ModelAttribute Teacher teacher
    @ResponseBody
    public Result save(@RequestBody Teacher teacher) {  // 老师自己存，工作人员存
        HttpSession session = request.getSession();
        Result result = new Result();
//        Gson gson = new Gson();
//        String name = request.getParameter("name");
//        String number = request.getParameter("number");
//        String teacherJsonStr = "{\"name\":\"" + name + "\",\"number\":\"" + number + "\"}";
//        Teacher teacher1 = gson.fromJson(teacherJsonStr,Teacher.class);
//        System.out.println(teacherJsonStr);
        String userRole = session.getAttribute("role").toString();
        boolean rightRole = "teacher".equals(userRole) || "manager".equals(userRole);
        if (rightRole) {
            int num = service.select(teacher.getNumber()).size();
            if (num == 1) {
                //更新
               // service.updateCourses(teacher1); //不对
                System.out.println("已保存过！");
                result.setCode("54");
                result.setMessage("已保存过！");
            } else if (num == 0) {
                boolean flag = service.save(teacher);
                if (flag) {
                    System.out.println("保存成功！");
                    result.setCode("200");
                    result.setMessage("保存成功");
                }
            } else {
                System.out.println("保存失败！");
                result.setCode("12");
                result.setMessage("保存失败，格式转换出现错误");
            }
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/selectTeacherCourse") //老师自己搜索自己的课程
    @ResponseBody
    public Result select() {
        HttpSession session = request.getSession();
        Result result = new Result();
        if ("teacher".equals(session.getAttribute("role"))) {
            String name = session.getAttribute("username").toString();
            System.out.println(name);
            System.out.println(service.select(name).size());
            // 找出第一个
            Teacher teacher = service.select(name).get(0);
            List<Course> courses = teacher.getCourses();
            System.out.println("课程数目为：" + courses.size());
            System.out.println("查询成功！");
            result.setCode("200");
            result.setMessage("查询成功");
            result.setData(courses);
        } else if ("manager".equals(session.getAttribute("role"))) {
            String number = request.getParameter("number");
            Teacher teacher = service.selectByNum(number);
            List<Course> courses = teacher.getCourses();
            result.setCode("200");
            result.setMessage("查询成功");
            result.setData(courses);
        }else {
            System.out.println("查询失败");
            result.setCode("31");
            result.setMessage("身份不符");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/deleteTeacher")
    @ResponseBody
    public Result delete(@RequestParam String number) { // 要删除的老师的工号
        HttpSession session = request.getSession();
        Result result = new Result();

        String manager = "manager";
        String role = "role";
        if (manager.equals(session.getAttribute(role))) {

            // 找出该老师所有信息
            Teacher teacher = service.selectByNum(number);
            if (teacher != null) {
                List<Course> courses = teacher.getCourses();
                for (Course course : courses) {
                    // 删除该老师的选课学生记录.
                    studentCourseService.deleteByCourse(course);
                }
                service.delete(teacher);
                System.out.println(teacher.getCourses().size());
                System.out.println("删除成功！");
                result.setCode("200");
                result.setMessage("删除成功");
            } else {
                System.out.println("该记录不存在！");
                result.setCode("42");
                result.setMessage("该记录不存在！");
            }

        } else {
            System.out.println("删除失败");
            result.setCode("41");
            result.setMessage("身份不符");
        }
        return result;
    }

    @PostMapping(value = "/haveLogin/updateTeacherCourse")
    @ResponseBody
    public Result updateCourse(@RequestBody Course course) {
        HttpSession session = request.getSession();
        Result result = new Result();
        String teacherName = session.getAttribute("username").toString();
        Teacher teacher = service.select(teacherName).get(0);
        if (teacher == null) {
            result.setCode("52");
            result.setMessage("该教师没有资料");
        } else {
            List<Course> courses = teacher.getCourses();
            courses.add(course);
            teacher.setCourses(courses);
            System.out.println(teacher.getCourses().size());
            service.save(teacher);
            result.setCode("200");
            result.setMessage("更新成功！");
        }
        return result;
    }
}
