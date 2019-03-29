package com.example.education.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ASUS
 */
@Entity(name = "Course")
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue
    private Integer id;
    private String number;
    private String name;
    private String time;
    private String place;
    private double grade;
    @JsonIgnore

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<StudentCourse> studentCourses = new ArrayList<>();

    public void setStudentCourses(List<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public List<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public Integer getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }

    public double getGrade() {
        return grade;
    }

//    public List<Student> getStudents() {
//        return students;
//    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

//    public void setStudents(List<Student> students) {
//        this.students = students;
//    }
}
