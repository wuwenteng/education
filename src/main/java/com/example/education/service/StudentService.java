package com.example.education.service;

import com.example.education.user.Student;
import com.example.education.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ASUS
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class StudentService {
    @Autowired
    private StudentRepository repository;

    public void save(Student student) {
        repository.save(student);
    }

    public void delete(Student student) {
        repository.delete(student);
    }

    public List<Student> select(String name) {
        return repository.findByName(name);
    }

    public Student findByNum(String number) {
        return repository.findByNumber(number);
    }

    public Student findById(int id) {
        return repository.findById(id);
    }

    public List<Student> findAll() {
        return repository.findAll();
    }
}
