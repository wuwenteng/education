package com.example.education.repository;

import com.example.education.user.Course;
import com.example.education.user.Student;
import com.example.education.user.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ASUS
 */
@Transactional
public interface StudentCourseRepository extends JpaRepository<StudentCourse,Integer> {
    List<StudentCourse> findByCourse(Course course);
    List<StudentCourse> findByStudent(Student student);
    StudentCourse findByStudentAndCourse(Student student, Course course);

    @Modifying
    @Query(value = "update StudentCourse sc set sc.score = :score where sc.student = :student and sc.course = :course")
    void updateScore(@Param("student") Student student, @Param("course") Course course, @Param("score") double score);

    @Modifying
    @Query(value = "update StudentCourse sc set sc.course = :course where sc.id = :id")
    void updateCourse(@Param("id") int id, @Param("course") Course course);
}
