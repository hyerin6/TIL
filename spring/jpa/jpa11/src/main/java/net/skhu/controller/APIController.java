package net.skhu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.domain.Course;
import net.skhu.domain.Department;
import net.skhu.domain.Professor;
import net.skhu.domain.Registration;
import net.skhu.domain.Student;
import net.skhu.repository.DepartmentRepository;
import net.skhu.repository.ProfessorRepository;
import net.skhu.repository.StudentRepository;

@RestController
@RequestMapping("api")
public class APIController {

    @Autowired DepartmentRepository departmentRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired ProfessorRepository professorRepository;

    @RequestMapping("departments")
    public List<Department> departments() {
        return departmentRepository.findAll();
    }

    @RequestMapping("students")
    public List<Student> students() {
        return studentRepository.findAll();
    }

    @RequestMapping("department/{id}/students")
    public List<Student> departmentStudents(@PathVariable("id") int id) {
        Department department = departmentRepository.findById(id).get();
        return department.getStudents();
    }

    @RequestMapping("department/{id}/professors")
    public List<Professor> departmentProfessors(@PathVariable("id") int id) {
        Department department = departmentRepository.findById(id).get();
        return department.getProfessors();
    }

    @RequestMapping("student/{id}/registrations")
    public List<Registration> studentRegistrations(@PathVariable("id") int id) {
        Student student = studentRepository.findById(id).get();
        return student.getRegistrations();
    }

    @RequestMapping("student/{id}/courses")
    public List<Course> studentCourses(@PathVariable("id") int id) {
        Student student = studentRepository.findById(id).get();
        List<Course> list = new ArrayList<Course>();
        for (Registration r : student.getRegistrations())
            list.add(r.getCourse());
        return list;

        /*
         return studentRepository
            .findById(id).get()
            .getRegistrations()
            .stream()
            .map(s -> s.getCourse());
         */
    }
}
