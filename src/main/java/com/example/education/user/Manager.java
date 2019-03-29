package com.example.education.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/*
* @2018-09-08
*/
@Data
@Entity
@Table(name = "manager")
public class Manager {
    @Id
    @GeneratedValue
    private int id;
    private String number;
    private String name;
}
