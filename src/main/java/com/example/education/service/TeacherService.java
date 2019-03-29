package com.example.education.service;

import com.example.education.user.Teacher;
import com.example.education.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ASUS
 */
@Transactional
@Service
public class TeacherService {
    @Autowired
    private TeacherRepository repository;

    public boolean save(Teacher teacher) {
        List<Teacher> teachers = repository.findByName(teacher.getName());
        repository.save(teacher);
        return true;
    }

    public void delete(Teacher teacher) {
        repository.delete(teacher);
    }

    public List<Teacher> select(String name) {
        return repository.findByName(name);
    }

    public Teacher selectByNum(String number) {
        return repository.findByNumber(number);
    }

    public void updateNumber(Teacher teacher) {
        repository.updateNumber(teacher.getName(), teacher.getNumber());
    }

    public List<Teacher> findAll() {
        return repository.findAll();
    }
}
