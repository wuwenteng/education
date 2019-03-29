package com.example.education.contrroller;

import com.example.education.service.CourseService;
import com.example.education.service.GradeService;
import com.example.education.service.StudentCourseService;
import com.example.education.service.StudentService;
import com.example.education.user.*;
import org.apache.el.parser.BooleanNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.font.TrueTypeFont;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@Controller
public class StudentController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    StudentService service;
    @Autowired
    CourseService courseService;
    @Autowired
    GradeService gradeService;
    @Autowired
    StudentService studentService;
    @Autowired
    StudentCourseService studentCourseService;

    @PostMapping(value = "/haveLogin/saveStudent")
    @ResponseBody
    public Result save(@RequestBody Student student) {

        Result result = new Result();
        service.save(student);
        System.out.println("保存成功");
        result.setUser(null);
        result.setCode("200");
        result.setMessage("保存成功");
        return result;
    }
/*
* grade表的操作，多对多关系中间表没有额外字段
*/
//    @GetMapping(value = "/addStudentCourse")  //会触发保存Grade表，在这里保存
//    @ResponseBody
//    public Result addCourse(@RequestParam String[] message) {
//        HttpSession session = request.getSession();
//        Result result = new Result();
//        String studentName = session.getAttribute("username").toString();
//        Student student = service.select(studentName).get(0);
//        //System.out.println(message);
//        if (student == null) {
//            result.setData(null);
//            result.setCode("62");
//            result.setMessage("该学生没有资料");
//        } else {
//            //List<Course> courses = student.getCourses();
//            List<Course> courses = new ArrayList<>();
//            List<StudentCourse> studentCourses = student.getStudentCourses();
//            List<Course> courseList = new ArrayList<>();
//            if (studentCourses != null) {
//                for (StudentCourse studentCourse : studentCourses) {
//                    Course course = studentCourse.getCourse();
//                    courses.add(course);
//                }
//            }
//
//            if (courses != null) {
//                for (String str : message) {
//                    System.out.println(str);
//                    Course course = courseService.selectByName(str);
//                    int flag = 1;
//                    for (Course course1 : courses) {
//                        if (str.equals(course1.getName())) {
//                            System.out.println("已有该课程");
//                            flag = 1;
//                            break;
//                        } else {
//                            flag = 0;
//                        }
//                    }
//                    if (flag == 0) {
//                        courseList.add(course);
//                    }
//                }
//            } else {
//                courses = new ArrayList<>();
//                for (String str : message) {
//                    Course course = courseService.selectByName(str);
//                    if (course != null) {
//                        courses.add(course);
//                    }
//                }
//            }
//            for (String str : message) {
//                Course course = courseService.selectByName(str);
//                Grade grade = gradeService.selectByStuAndCour(student.getNumber(),course.getNumber());
//                if (grade == null) {
//                    Grade grade1 = new Grade();
//                    grade1.setCourseName(course.getName());
//                    grade1.setCourseNum(course.getNumber());
//                    grade1.setStudentName(student.getName());
//                    grade1.setStudentNum(student.getNumber());
//                    gradeService.save(grade1);
//                }
//            }
//            System.out.println(courseList.size());
//            if (courseList.size() != 0) {
//                courses.addAll(courseList);
//            }
//            //student.setCourses(courses);
//            System.out.println(student.getCourses().size());
//            service.save(student);
//            result.setData(null);
//            result.setCode("63");
//            result.setMessage("更新成功！");
//        }
//        return result;
//    }

    @GetMapping(value = "/haveLogin/addStudentCourse")
    @ResponseBody
    public Result addCourse(@RequestParam String[] message) { // 传入的是课程的编号
        HttpSession session = request.getSession();
        Result result = new Result();
        String username = session.getAttribute("username").toString();
        String role = session.getAttribute("role").toString();
        if (role != null) {
            Student student = studentService.select(username).get(0);
            System.out.println(student.getId());
            List<StudentCourse> studentCourses = studentCourseService.selectByStudent(student);
            List<StudentCourse> studentCourseList = new ArrayList<>();
            for (String str : message) {
                Course course = courseService.select(str);
                System.out.println(course.getId());
                boolean flag = true;
                for (StudentCourse sc:studentCourses) {
                    if (course.equals(sc.getCourse())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    StudentCourse sc1 = new StudentCourse();
                    sc1.setCourse(course);
                    sc1.setStudent(student);
                    studentCourseList.add(sc1);
                    studentCourseService.save(sc1);
                }
            }
            studentCourses.addAll(studentCourseList);
            result.setCode("200");
            result.setMessage("添加课程成功");
            result.setData(studentCourses);
        } else {
            result.setCode("101");
            result.setMessage("添加课程失败，您还未登录");
        }
        return result;
    }

    /**
     * 所有角色都能删除，只在教务人员那有链接
     *
     * @return Result包含是否成功和相应需要的返回信息
     */
    @GetMapping(value = "/haveLogin/deleteStudent")
    @ResponseBody
    public Result delete() {  // @RequestParam String number
        //根据学号删除学生
        Result result = new Result();
        HttpSession session = request.getSession();
        String studentName = session.getAttribute("username").toString();
        String number = request.getParameter("number");

        if (studentName != null) {
            Student student = service.findByNum(number);
            if (student != null) {
                //List<Grade> grades = gradeService.selectByStudentNum(student.getNumber());
                //for (Grade grade : grades) {
                    //gradeService.delete(grade);
                //}
                studentCourseService.deleteByStudent(student);
                service.delete(student);
                System.out.println("删除成功");
                result.setCode("200");
                result.setMessage("删除成功");
            } else {
                 System.out.println("删除失败，该学生不存在");
                result.setCode("65");
                result.setMessage("删除失败，该学生不存在");
            }
        } else {
            System.out.println("删除失败，未登录");
            result.setCode("66");
            result.setMessage("删除失败，未登录");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/selectStudentCourse")
    @ResponseBody
    public Result select() {  // @RequestParam String number

        HttpSession session = request.getSession();
        Result result = new Result();
        String role = session.getAttribute("role").toString();
        String number = request.getParameter("number");
        System.out.println(number);
        if (role != null) {
            Student student = studentService.findByNum(number);
            if (student != null) {
                List<StudentCourse> studentCourses = studentCourseService.selectByStudent(student);
                System.out.println(studentCourses.size());
                List<Course> courses = new ArrayList<>();
                for (StudentCourse sc:studentCourses) {
                    Course course = sc.getCourse();
                    course.setGrade(sc.getScore());
                    courses.add(course);
                }
                result.setData(courses);
                result.setCode("200");
                result.setMessage("查询成绩成功");
            } else {
                result.setCode("103");
                result.setMessage("查询成绩失败，没有该学生");
            }
        } else {
            result.setCode("101");
            result.setMessage("查询成绩失败，您还未登录");
        }
        return result;
    }

    @GetMapping(value = "/haveLogin/selectStudent")
    @ResponseBody
    public Result selectAll() {
        HttpSession session = request.getSession();
        Result result = new Result();
        String name = session.getAttribute("username").toString();
        // 这个应该是教务处人员才能看到
        if (name != null) {
            List<Student> students = service.findAll();
            System.out.println(students.size());
            result.setMessage("查询成功");
            result.setCode("200");
            result.setData(students);

        } else {
            result.setMessage("查询失败");
            result.setCode("101");
        }
        result.setUser(null);
        return result;
    }
}
