package com.example.education.contrroller;

import com.example.education.service.CourseService;
import com.example.education.service.StudentCourseService;
import com.example.education.service.StudentService;
import com.example.education.service.TeacherService;
import com.example.education.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentCourseController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    CourseService courseService;

    @GetMapping(value = "/haveLogin/selectByCourseNumber")
    @ResponseBody
    public Result selectByCourseNum(@RequestParam String courseNum) {
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
                Course course = courseService.select(courseNum);
                List<StudentCourse> studentCourses = studentCourseService.selectByCourse(course);
                result.setData(studentCourses);
                result.setCode("70");
                result.setMessage("查询成功！");
            } else {
                result.setCode("72");
                result.setMessage("没有这门课");
            }
        } else {
            result.setCode("79");
            result.setMessage("未登录或不是老师");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/updateScore")
    @ResponseBody
    public Result updateScore() {
        HttpSession session = request.getSession();
        Result result = new Result();
        String name = session.getAttribute("username").toString();
        String role = session.getAttribute("role").toString();
        String studentNum = request.getParameter("studentNum");
        System.out.println(studentNum);
        String courseNum = request.getParameter("courseNum");
        double score = Double.valueOf(request.getParameter("score"));
        System.out.println(score);
        Student student = studentService.findByNum(studentNum);
        Course course1 = courseService.select(courseNum);
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourse(course1);
        studentCourse.setStudent(student);
        studentCourse.setScore(score);
        boolean flag = false;
        if ("teacher".equals(role)) {
            Teacher teacher = teacherService.select(name).get(0);
            List<Course> courses = teacher.getCourses();
            for (Course course : courses) {
                if (studentCourse.getCourse().equals(course)) {
                    flag = true;
                }
            }
            if (flag) {
                studentCourseService.updateScore(studentCourse);
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

    @PostMapping(value = "/haveLogin/deleteScore")
    @ResponseBody
    public Result deleteGrade() {  // 什么情况下会触发删除成绩？删除学生，删除课程。。。
        HttpSession session = request.getSession();
        Result result = new Result();
        String role = session.getAttribute("role").toString();
        String studentNum = request.getParameter("studentNumber");
        String courseNum = request.getParameter("courseNumber");
        Student student = studentService.findByNum(studentNum);
        Course course = courseService.select(courseNum);

        StudentCourse studentCourse = studentCourseService.selectByStudentAndCourse(student,course);
        if ("teacher".equals(role)) {
            if (studentCourse != null) {

                studentCourseService.delete(studentCourse);
                result.setCode("200");
                result.setMessage("删除成功");
            } else {
                result.setCode("76");
                result.setMessage("删除失败，没有这门课");
            }

        }else {
            result.setCode("79");
            result.setMessage("不是教师");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/selectRecord")
    @ResponseBody
    public Result selectRecord() {
        HttpSession session = request.getSession();
        Result result = new Result();
        String role = session.getAttribute("role").toString();
        if (role != null) {
            List<StudentCourse> studentCourses = studentCourseService.selectAll();
            result.setData(studentCourses);
            result.setCode("200");
            result.setMessage("查询成功");
        } else {
            result.setCode("131");
            result.setMessage("查询失败，您尚未登录");
        }
        return result;
    }
}
