package com.example.education.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ASUS
 * 审批改课表
 */
@Data
@Entity
@Table(name = "approve")
public class Approve {
    @Id
    @GeneratedValue
    private Integer id;
    private String deleteName;
    private String deleteNumber;
    private String addName;
    private String addNumber;
    private String state;
    private String reason;
    private String studentNumber;
    private String operator;
}
