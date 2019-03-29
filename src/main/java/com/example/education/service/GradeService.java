package com.example.education.service;

import com.example.education.user.Grade;
import com.example.education.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ASUS
 */
@Transactional
@Service
public class GradeService {
    @Autowired
    GradeRepository repository;

    public void save(Grade grade) {
        repository.save(grade);
    }

    public void delete(Grade grade) {
        repository.delete(grade);
    }

    public List<Grade> selectByStudentNum(String studentNum) {
        return repository.findByStudentNum(studentNum);
    }

    public List<Grade> selectByStudentName(String studentName) {
        return repository.findByStudentName(studentName);
    }

    public List<Grade> selectByCourseNum(String courseNum) {
        return repository.findByCourseNum(courseNum);
    }

    public List<Grade> selectByCourseName(String courseName) {
        return repository.findByCourseName(courseName);
    }

    public void update(Grade grade) {
        repository.update(grade.getStudentNum(), grade.getCourseNum(), grade.getGrade());
    }
    public Grade selectByStuAndCour (String studentNum, String courseNum) {
        return repository.findByStudentNumAndCourseNum(studentNum, courseNum);
    }

    public void deleteByCourseNum(String courseNum) {
        List<Grade> grades = repository.findByCourseNum(courseNum);
        for (Grade grade : grades) {
            delete(grade);
        }
    }
}
