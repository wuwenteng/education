package com.example.education.user;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ASUS
 */
@Data
@Entity
@Table(name = "studentcourse")
public class StudentCourse {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id",nullable = true)
    private Student student;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id",nullable = true)
    private Course course;
    private double score;


}
