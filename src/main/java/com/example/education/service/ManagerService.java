package com.example.education.service;

import com.example.education.user.Manager;
import com.example.education.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ManagerService {
    @Autowired
    ManagerRepository repository;

    public void save(Manager manager) {
        repository.save(manager);
    }

    public void delete(Manager manager) {
        repository.delete(manager);
    }

    public Manager selectByNum(String number) {
        return repository.findByNumber(number);
    }

    public List<Manager> selectByName(String name) {
        return repository.findByName(name);
    }

    public void updateName(Manager manager) {
        repository.updateName(manager.getNumber(), manager.getName());
    }
}
