package com.example.education.repository;

import com.example.education.user.Manager;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager,Integer> {
    Manager findByNumber(String number);
    List<Manager> findByName(String name);
    @Modifying
    @Query(value = "update Manager m set m.name = :name where m.number = :number")
    void updateName(@Param("number") String number, @Param("name") String name);
}
