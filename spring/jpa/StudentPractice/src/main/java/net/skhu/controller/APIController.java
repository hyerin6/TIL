package net.skhu.controller;

import net.skhu.domain.Course;
import net.skhu.domain.Register;
import net.skhu.domain.Student;
import net.skhu.repository.CourseRepository;
import net.skhu.repository.DepartmentRepository;
import net.skhu.repository.RegisterRepository;
import net.skhu.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    RegisterRepository registerRepository;

    // course id에 맞는 student 목록 출력
    @RequestMapping("course/{id}/students")
    public ResponseEntity<Map<String, Object>> courseList(@PathVariable("id") int id){
        Map<String, Object> res = new HashMap<String, Object>();
        Course course  = courseRepository.findById(id).get();
        List<Register> registers = course.getRegisterList();
        List<Student> list = new ArrayList<>();
        for(Register register : registers){
            list.add(register.getStudent());
        }
        res.put("course", list);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
