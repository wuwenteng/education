package com.example.education.repository;

import com.example.education.user.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CourseRepository extends JpaRepository<Course,Integer> {
    Course findByName(String name);
    Course findByNumber(String number);
    List<Course> findByPlace(String place);
    List<Course> findByTime(String time);
    @Modifying
    @Query(value = "update Course u set u.time = :time where u.number = :num")
    void updateTime(@Param("num") String num, @Param("time") String time);
    @Modifying
    @Query(value = "update Course u set u.place = :place where u.number = :num")
    void updatePlace(@Param("num") String num,@Param("place") String place);
}
