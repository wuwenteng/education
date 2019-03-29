package com.example.education.repository;

import com.example.education.user.Approve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ASUS
 */
@Transactional
public interface ApproveRepository extends JpaRepository<Approve,Integer> {
    /**
     * 通过删除课号与添加课号与学号查找记录
     *
     * @param deleteNumber 删除课程课号
     * @param addNumber 添加课程课号
     * @param studentNumber 学生学号
     * @return Approve
     */
    public abstract Approve findByDeleteNumberAndAddNumberAndStudentNumber(String deleteNumber, String addNumber, String studentNumber);

    /**
     * 根据学生学号搜索记录
     *
     * @param studentNumber 学生学号
     * @return List<Approve>
     */
    public abstract List<Approve> findByStudentNumber(String studentNumber);

    public abstract List<Approve> findByState(String state);

    public abstract Approve findById(int id);
    /**
     * 更新审核进程
     *
     * @param id 记录的编号
     * @param state 审核的描述
     */
    @Modifying
    @Query(value = "update Approve a set a.state = :state where a.id = :id")
    public abstract void update(@Param("id") int id, @Param("state") String state);

    /**
     * 更新操作员信息
     * @param id 记录的编号
     * @param operator 操作员编号
     */
    @Modifying
    @Query(value = "update Approve a set a.operator = :operator where a.id = :id")
    public abstract void updateOperator(@Param("id") int id, @Param("operator") String operator);
}
