package com.example.education.contrroller;

import com.example.education.service.CourseService;
import com.example.education.service.GradeService;
import com.example.education.service.StudentService;
import com.example.education.service.TeacherService;
import com.example.education.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ASUS
 */
@CrossOrigin(origins = "*")
@Controller
public class GradeController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    GradeService gradeService;
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;

    @GetMapping(value = "/haveLogin/selectByStudentNum")
    @ResponseBody
    public Result selectByStudentNum(@RequestParam String studentNum) { //学生有权利使用
        HttpSession session = request.getSession();
        Result result = new Result();
        String name = session.getAttribute("username").toString();
        String role = session.getAttribute("role").toString();
        boolean flag = false;
        if ("student".equals(role)) {
            List<Student> students = studentService.select(name);
            for (Student stu : students) {
                if (studentNum.equals(stu.getNumber())) {
                    flag = true;
                }
            }
            if (flag) {
                List<Grade> grades = gradeService.selectByStudentNum(studentNum);
                result.setData(grades);
                result.setCode("70");
                result.setMessage("查询成绩成功！");
            } else {
                result.setData(null);
                result.setCode("71");
                result.setMessage("查询学号与名称不符");
            }
        } else {
            result.setData(null);
            result.setCode("79");
            result.setMessage("未登录或不是学生");

        }
        result.setUser(null);
        return result;
    }

    @GetMapping(value = "/haveLogin/selectByCourseNum")
    @ResponseBody
    public Result selectByCourseNum(@RequestParam String courseNum) {  //老师有权利使用
        System.out.println("选课记录");
        System.out.println(courseNum);
        HttpSession session = request.getSession();
        Result result = new Result();
        String name = session.getAttribute("username").toString();
        String role = session.getAttribute("role").toString();
        boolean flag = false;
        if ("teacher".equals(role)) {
            Teacher teacher = teacherService.select(name).get(0);
            List<Course> courses = teacher.getCourses();
            for (Course course : courses) {
                if (courseNum.equals(course.getNumber())) {
                    flag = true;
                }
            }
            System.out.println("flag："+ flag);
            if (flag) {
                List<Grade> grades = gradeService.selectByCourseNum(courseNum);
                result.setData(grades);
                result.setCode("70");
                result.setMessage("查询成绩成功！");
            } else {
                result.setData(null);
                result.setCode("72");
                result.setMessage("没有这门课");
            }
        } else {
            result.setData(null);
            result.setCode("79");
            result.setMessage("未登录或不是老师");

        }
        result.setUser(null);
        return result;
    }

    @PostMapping(value = "/haveLogin/updateGrade")
    @ResponseBody
    public Result updateGrade(@RequestBody Grade grade) {
        HttpSession session = request.getSession();
        Result result = new Result();
        String name = session.getAttribute("username").toString();
        String role = session.getAttribute("role").toString();
        boolean flag = false;
        if ("teacher".equals(role)) {
            Teacher teacher = teacherService.select(name).get(0);
            List<Course> courses = teacher.getCourses();
            for (Course course : courses) {
                if (grade.getCourseNum().equals(course.getNumber())) {
                    flag = true;
                }
            }
            if (flag) {
                gradeService.update(grade);
                result.setCode("73");
                result.setMessage("更新成功");
            } else {
                result.setCode("74");
                result.setMessage("更新失败，没有这门课");
            }
        } else {
            result.setCode("79");
            result.setMessage("没有登陆或不是教师");
        }
        return result;
    }

    @PostMapping(value = "/haveLogin/deleteGrade")
    @ResponseBody
    public Result deleteGrade(@RequestBody Grade grade) {  // 什么情况下会触发删除成绩？删除学生，删除课程。。。
        HttpSession session = request.getSession();
        Result result = new Result();
        String name = session.getAttribute("username").toString();
        String role = session.getAttribute("role").toString();
        boolean flag = false;
        if ("teacher".equals(role)) {
            Teacher teacher = teacherService.select(name).get(0);
            List<Course> courses = teacher.getCourses();
            for (Course course : courses) {
                if (grade.getCourseNum().equals(course.getNumber())) {
                    flag = true;
                }
            }
            if (flag) {
                gradeService.delete(grade);
                result.setCode("73");
                result.setMessage("更新成功");
            } else {
                result.setCode("74");
                result.setMessage("更新失败，没有这门课");
            }
        } else {
            result.setCode("79");
            result.setMessage("没有登陆或不是教师");
        }
        result.setUser(null);
        result.setData(null);
        return result;
    }
}
