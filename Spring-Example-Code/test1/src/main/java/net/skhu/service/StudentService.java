package net.skhu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.dto.Student;
import net.skhu.mapper.StudentMapper;

@Service
public class StudentService {

    @Autowired
    StudentMapper studentMapper;

    public List<Student> findAll() {
        return studentMapper.findAll();
    }

    public Student findById(int id) {
        return studentMapper.findById(id);
    }

    public void insert(Student student) throws InvalidDataException {
        Student s = studentMapper.findByStudentNumber(student.getStudentNumber());
        if (s != null)
            throw new InvalidDataException("학번이 중복됩니다");
        else
            studentMapper.insert(student);
    }
}
