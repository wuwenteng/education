package com.example.education.repository;

import com.example.education.user.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface GradeRepository extends JpaRepository<Grade,Long> {
    List<Grade> findByStudentName(String studentName);
    List<Grade> findByCourseName(String courseName);
    List<Grade> findByStudentNum(String studentNum);
    List<Grade> findByCourseNum(String courseNum);
    Grade findByStudentNumAndCourseNum(String studentNum, String courseNum);
    @Modifying
    @Query(value = "update Grade g set g.grade = :grade where g.studentNum = :studentNum and g.courseNum = :courseNum")
    void update(@Param("studentNum") String studentNum, @Param("courseNum") String courseNum, @Param("grade") double grade);
}
