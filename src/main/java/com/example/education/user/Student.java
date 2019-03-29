package com.example.education.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.tomcat.util.security.PrivilegedSetTccl;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String number;

    @JsonIgnore
//    @ManyToMany(cascade = {
//            CascadeType.PERSIST,
//            CascadeType.MERGE
//    })
//    @JoinTable(name = "student_course",
//            joinColumns = @JoinColumn(name = "student_id"),
//            inverseJoinColumns = @JoinColumn(name = "course_id")
//    )
//    private List<Course> courses = new ArrayList<>();
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<StudentCourse> studentCourses = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public List<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setStudentCourses(List<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }
}
