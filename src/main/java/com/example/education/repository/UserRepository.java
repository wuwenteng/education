package com.example.education.repository;

import com.example.education.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

/**
 * @author ASUS
 */
@Transactional
public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findByUsername(String username);
    List<User> findByUsernameAndRole(String username, String role);
    List<User> findByRole(String role);

    @Modifying
    @Query(value = "update User u set u.password = :password where u.username = :username")
    void update(@Param("username") String username, @Param("password") String password);

    @Modifying
    @Query(value = "update User u set u.imagePath = :imagePath where u.username = :username")
    void updateImage(@Param("username") String username, @Param("imagePath") String imagePath);

}
