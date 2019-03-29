package com.example.education.service;

import com.example.education.user.Course;
import com.example.education.user.Student;
import com.example.education.user.StudentCourse;
import com.example.education.repository.StudentCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ASUS
 */
@Service
@Transactional
public class StudentCourseService {
    @Autowired
    StudentCourseRepository repository;

    public void save(StudentCourse studentCourse) {
        repository.save(studentCourse);
    }

    public void delete(StudentCourse studentCourse) {
        studentCourse.setCourse(null);
        studentCourse.setStudent(null);
        repository.delete(studentCourse);
    }

    public List<StudentCourse> selectByStudent(Student student) {
        return repository.findByStudent(student);
    }

    public List<StudentCourse> selectByCourse(Course course) {
        return repository.findByCourse(course);
    }

    public void updateScore(StudentCourse studentCourse) {
        repository.updateScore(studentCourse.getStudent(),studentCourse.getCourse(),studentCourse.getScore());
    }

    public void updateCourse(StudentCourse studentCourseDel, Course courseAdd) {
        repository.updateCourse(studentCourseDel.getId(), courseAdd);
    }

    public List<StudentCourse> selectAll() {
        return repository.findAll();
    }

    public void deleteByCourse(Course course) {
        List<StudentCourse> studentCourses = selectByCourse(course);
        for (StudentCourse sc:studentCourses) {
            sc.setCourse(null);
            sc.setStudent(null);
            delete(sc);
        }
    }

    public void deleteByStudent(Student student) {
        List<StudentCourse> studentCourses = selectByStudent(student);
        for (StudentCourse sc:studentCourses) {
            sc.setCourse(null);
            sc.setStudent(null);
            delete(sc);
        }
    }

    public StudentCourse selectByStudentAndCourse(Student student, Course course) {
        return repository.findByStudentAndCourse(student, course);
    }
}
