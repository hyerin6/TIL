package net.skhu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.dto.ActionResult;
import net.skhu.dto.Student;
import net.skhu.service.InvalidDataException;
import net.skhu.service.StudentService;

@RestController
public class StudentAPIController {

    @Autowired StudentService studentService;

    @RequestMapping("/api/students")
    public List<Student> students() {
        return studentService.findAll();
    }

    @RequestMapping("/api/student/{id}")
    public Student student(@PathVariable("id") int id) {
        return studentService.findById(id);
    }

    @RequestMapping(value="/api/student", method=RequestMethod.POST)
    public ActionResult studentPost(@RequestBody Student student) {
        try {
            studentService.insert(student);
            return new ActionResult(true, "저장성공");
        } catch (InvalidDataException ex) {
            return new ActionResult(false, ex.getMessage());
        }
    }
}
