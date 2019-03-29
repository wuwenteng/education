package com.example.education.contrroller;

import com.example.education.service.ApproveService;
import com.example.education.service.CourseService;
import com.example.education.service.StudentCourseService;
import com.example.education.service.StudentService;
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
@Controller
@RequestMapping(value = "/haveLogin")
public class ApproveController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    ApproveService approveService;
    @Autowired
    StudentService studentService;
    @Autowired
    CourseService courseService;
    @Autowired
    StudentCourseService studentCourseService;

    // @GetMapping(value = "/haveLogin/updateApprove")


    /**
     * @param managerNumber 教务人员的number
     * @param approveId 申请调课记录的id
     * @param approve 更新的内容
     * @return result 返回结果
     */
    @PutMapping(value = "/manager/{managerNumber}/approves/{approveId}")
    @ResponseBody
    public Result update(@PathVariable String managerNumber, @PathVariable int approveId,
                                @RequestBody Approve approve) {

        Result result = new Result();
        boolean isNull = approveService.select(approveId) == null;
        if (isNull) {
            result.setCode("201");
            result.setMessage("失败，没有相应课程，重新提交申请");
        } else {
//            String state = "通过";
            Approve approve1 = approveService.select(approveId);
            approve1.setState(approve.getState());
            //approveService.update(deleteCourseNumber,addCourseNumber,studentNumber,state,operator);
            // 通过审核后，将该学生的选课进行修改。
            String stateStr = "通过";
            if (stateStr.equals(approve.getState())) {
                changeStudentCourse(approve.getStudentNumber(), approve.getDeleteNumber(),approve.getAddNumber());
            }

            result.setCode("200");
        }
        return result;
    }

    @PostMapping(value = "/student/{studentNumber}/approves")
    @ResponseBody
    public Result create( @RequestBody Approve approve, @PathVariable String studentNumber ) {
        System.out.println(approve.getAddNumber());
        System.out.println("save approve");
        Result result = new Result();
        HttpSession session = request.getSession();
        // 比较url中的studentNumber是否是登录的学生本身
        int flag = 0;
        if (studentNumber.equals(approve.getStudentNumber())) {
            flag = approveService.save(approve);
        }

        if (flag == 1) {
            result.setCode("200");
        } else {
            result.setCode("202");
            result.setMessage("保存失败");
        }
        return result;
    }

    @GetMapping(value = "/students/{studentNumber}/approves")
    @ResponseBody
    public Result<Approve> list(@PathVariable String studentNumber) {

        Result result = new Result();
        List<Approve> approves = approveService.selectByStudentNumber(studentNumber);
        if (approves != null) {
            result.setCode("200");
            result.setData(approves);
        } else {
            result.setMessage("没有记录");
            result.setCode("202");
        }
        return result;
    }

    @DeleteMapping(value = "/student/{studentNumber}/approves/{approveId}")
    @ResponseBody
    public Result delete(@PathVariable String studentNumber, @PathVariable int approveId) {
        Result result = new Result();
        Approve approve = approveService.select(approveId);
        if (approve.getStudentNumber().equals(studentNumber)) {
            approveService.delete(approve);
            result.setCode("200");
        } else {
            result.setCode("500");
            result.setMessage("只能删除自己的申请");
        }
        return result;
    }

    @GetMapping(value = "/manager/approves")
    @ResponseBody
    public Result<Approve> selectAll() {
        Result result = new Result();
        List<Approve> approves = approveService.selectAll();
        result.setData(approves);
        result.setCode("200");
        return result;
    }

    private void changeStudentCourse(String studentNumber, String delCourseNumber, String addCourseNumber) {
        Student student = studentService.findByNum(studentNumber);
        Course courseDel = courseService.select(delCourseNumber);
        Course courseAdd = courseService.select(addCourseNumber);
        StudentCourse studentCourseDel = studentCourseService.selectByStudentAndCourse(student, courseDel);
        studentCourseService.updateCourse(studentCourseDel,courseAdd);
    }
}
