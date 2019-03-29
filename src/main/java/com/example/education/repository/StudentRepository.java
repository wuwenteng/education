package com.example.education.repository;

import com.example.education.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface StudentRepository extends JpaRepository<Student,Integer> {
    List<Student> findByName(String name);
    Student findByNumber(String number);
    Student findById(int id);
}
