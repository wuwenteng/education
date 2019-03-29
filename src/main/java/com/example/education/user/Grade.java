package com.example.education.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue
    private Long id;
    private String studentNum;
    private String courseNum;
    private String studentName;
    private String courseName;
    private double grade;
}
