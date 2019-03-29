package com.example.education.user;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ASUS
 */
@Data
@Entity
@Table(name = "teacher")
public class Teacher {
    @Id
    @GeneratedValue
    private int id;
    private String number;
    private String name;

    /**
     * 开课目录
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "teacher_id")
    private List<Course> courses = new ArrayList<>();

    public void addCourse(Course course) {
        courses.add(course);
        //course.setTeacher(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        //course.setTeacher(null);
    }
}
