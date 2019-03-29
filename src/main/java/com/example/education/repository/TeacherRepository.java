package com.example.education.repository;

import com.example.education.user.Teacher;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TeacherRepository extends JpaRepository<Teacher,Integer> {
    List<Teacher> findByName(String name);
    Teacher findByNumber(String number);
    @Modifying
    @Query(value = "update Teacher u set u.number = :number where u.name = :name")
    void updateNumber(@Param("name") String name, @Param("number") String number);

}
