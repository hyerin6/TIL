package net.skhu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.skhu.dto.Student;


@Mapper
public interface StudentMapper {
    Student findById(int id);
    Student findByStudentNumber(String studentNumber);
    List<Student> findAll();
    void update(Student student);
    void insert(Student student);
    void deleteById(int id);
}
