package com.example.education.contrroller;

import com.example.education.service.*;
import com.example.education.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.metamodel.ListAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ASUS
 */
@CrossOrigin(origins = "*")
@Controller
public class ManagerController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    ManagerService managerService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    StudentService studentService;
    @Autowired
    GradeService gradeService;
    @Autowired
    CourseService courseService;
    @Autowired
    StudentCourseService studentCourseService;

    @PostMapping(value = "/haveLogin/saveManager")
    @ResponseBody
    public Result saveManager(@RequestBody Manager manager) {
        HttpSession session = request.getSession();
        Result result = new Result();
        // username存为其他的number
        String number = session.getAttribute("username").toString();
        String role = session.getAttribute("role").toString();
        if ("manager".equals(role)) {
            Manager manager1 = managerService.selectByNum(number);
            if (manager1 == null) {
                managerService.save(manager);
                System.out.println("保存工作人员成功");
                result.setCode("200");
                result.setMessage("保存工作人员成功");
            } else {
                result.setCode("81");
                result.setMessage("保存工作人员失败，已有该人员");
            }
        } else {
            result.setCode("89");
            result.setMessage("您还未登录");
        }
        return result;
    }

    @PostMapping(value = "/haveLogin/updateManager")
    @ResponseBody
    public Result updateManager(@RequestBody Manager manager) {
        HttpSession session = request.getSession();
        Result result = new Result();
        // username存为其他的number
        String number = session.getAttribute("username").toString();
        String role = session.getAttribute("role").toString();
        if ("manager".equals(role)) {
            Manager manager1 = managerService.selectByNum(number);
            if (manager1 != null) {
                managerService.updateName(manager);
                result.setCode("200");
                result.setMessage("更新成功");
            } else {
                result.setCode("83");
                result.setMessage("更新工作人员失败，还没有该人员");
            }
        } else {
            result.setCode("89");
            result.setMessage("您还未登录");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/deleteManager")
    @ResponseBody
    public Result delete(String number) {
        HttpSession session = request.getSession();
        Result result = new Result();
        String role = session.getAttribute("role").toString();
        if ("manager".equals(role)) {
            Manager manager = managerService.selectByNum(number);
            if (manager != null) {
                managerService.delete(manager);
                result.setCode("200");
                result.setMessage("删除工作人员成功");
            } else {
                result.setCode("85");
                result.setMessage("删除工作人员失败，没有该人员");
            }
        } else {
            result.setCode("89");
            result.setMessage("您还未登录");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/addTeacherCourse")
    @ResponseBody
    public Result addTeacherCourse() {
        HttpSession session = request.getSession();
        Result result = new Result();
        String role = session.getAttribute("role").toString();

        String teacherNum = request.getParameter("number");
        Course course = new Course();
        course.setName(request.getParameter("courseName"));
        course.setNumber(request.getParameter("courseNumber"));
        course.setPlace(request.getParameter("coursePlace"));
        course.setTime(request.getParameter("courseTime"));

        if ("manager".equals(role)) {
            Teacher teacher = teacherService.selectByNum(teacherNum);
            if (teacher != null) {
                List<Course> courses = teacher.getCourses();
                courses.add(course);
                // 笨方法，已成功
                teacher.setCourses(courses);
                System.out.println(teacher.getCourses().size());
                teacherService.save(teacher);
                result.setCode("200");
                result.setMessage("添加成功");
            } else {
                result.setCode("87");
                result.setMessage("添加失败，没有该教师");
            }
        } else {
            result.setCode("89");
            result.setMessage("您还未登录");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/appendStudentCourse")
    @ResponseBody
    public Result addStudentCourse() {
        HttpSession session = request.getSession();
        Result result = new Result();
        String role = session.getAttribute("role").toString();

        String studentNum = request.getParameter("number");
        Course course = courseService.select(request.getParameter("courseNumber"));

        if ("manager".equals(role)) {
            Student student = studentService.findByNum(studentNum);
            if (student != null) {
                List<StudentCourse> studentCourses = studentCourseService.selectByStudent(student);
                List<Course> courses = new ArrayList<>();
                for (StudentCourse sc:studentCourses) {
                    Course course1 = sc.getCourse();
                    courses.add(course1);
                }
                // 检查是否有该课程
                int flag = 1;
                for (Course c: courses) {
                    if (c.getNumber().equals(course.getNumber())) {
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    // 存入grade信息
                    Grade grade = new Grade();
                    grade.setStudentNum(student.getNumber());
                    grade.setStudentName(student.getName());
                    grade.setCourseNum(course.getNumber());
                    grade.setCourseName(course.getName());
                    courses.add(course);
                    StudentCourse studentCourse = new StudentCourse();
                    studentCourse.setStudent(student);
                    studentCourse.setCourse(course);
                    studentCourses.add(studentCourse);
                    student.setStudentCourses(studentCourses);
                    studentService.save(student);
                    gradeService.save(grade);
                    result.setCode("200");
                    result.setMessage("添加成功");
                } else {
                    result.setCode("88");
                    result.setMessage("已有该课程");
                }
            } else {
                result.setCode("87");
                result.setMessage("添加失败，没有该学生");
            }
        } else {
            result.setCode("89");
            result.setMessage("您还未登录");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/selectMessage")
    @ResponseBody
    public Result selectMessage() {
        System.out.println("列表");
        HttpSession session = request.getSession();
        String role = session.getAttribute("role").toString();
        String selectRole = request.getParameter("selectRole");
        Result result = new Result();
        if ("manager".equals(role)) {
            if ("teacher".equals(selectRole)) {
                List<Teacher> teachers = teacherService.findAll();
                result.setData(teachers);
            } else if ("student".equals(selectRole)) {
                List<Student> students = studentService.findAll();
                result.setData(students);
            }
            result.setCode("200");
            result.setMessage("搜索成功");
        } else {
            result.setCode("801");
            result.setMessage("您不是管理员，或还未登录");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/selectRoleCourse")
    @ResponseBody
    public Result selectCourse() {
        System.out.println("课程");
        HttpSession session = request.getSession();
        String role = session.getAttribute("role").toString();
        String number = request.getParameter("number");
        Result result = new Result();
        if ("manager".equals(role)) {
            Teacher teacher = teacherService.selectByNum(number);
            Student student = studentService.findByNum(number);
            if (teacher != null) {
                List<Course> courses = teacher.getCourses();
                System.out.println(courses.size());
                result.setData(courses);
                result.setCode("200");
                result.setMessage("搜索成功");
            } else if (student != null) {
                List<StudentCourse> studentCourses = studentCourseService.selectByStudent(student);
                List<Course> courses = new ArrayList<>();
                for (StudentCourse sc:studentCourses) {
                    Course course = sc.getCourse();
                    courses.add(course);
                }

                result.setData(courses);
                result.setCode("200");
                result.setMessage("搜索成功");
                System.out.println(courses.size());
            } else {
                result.setCode("201");
                result.setMessage("学号/工号错误");
            }
        } else {
            result.setCode("801");
            result.setMessage("您不是管理员，或还未登录");
        }
        return result;
    }
}
