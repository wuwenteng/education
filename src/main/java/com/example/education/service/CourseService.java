package com.example.education.service;

import com.example.education.user.Course;
import com.example.education.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ASUS
 */
@Transactional
@Service
public class CourseService {
    @Autowired
    private CourseRepository repository;

    public void save(Course course) {
        repository.save(course);
    }

    public void delete(Course course) {
        repository.delete(course);
    }

    public Course select(String num) {
        Course course = repository.findByNumber(num);
        return course;
    }

    public Course selectByName(String name) {
        Course course = repository.findByName(name);
        return course;
    }

    public List<Course> selectByTime(String time) {
        List<Course> courses = repository.findByTime(time);
        return courses;
    }

    public void updateTime(Course course) {
        repository.updateTime(course.getNumber(),course.getTime());
    }

    public void updatePlace(Course course) {
        repository.updatePlace(course.getNumber(),course.getPlace());
    }

    public List<Course> selectAll() {
        return repository.findAll();
    }
}
