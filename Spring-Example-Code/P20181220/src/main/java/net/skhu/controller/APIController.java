package net.skhu.controller;

import net.skhu.domain.Employee;
import net.skhu.repository.AdressRepository;
import net.skhu.repository.DepartmentRepository;
import net.skhu.repository.EmployeeRepository;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired EmployeeRepository employeeRepository;
    @Autowired DepartmentRepository departmentRepository;
    @Autowired AdressRepository adressRepository;

    @RequestMapping(value="employees", method=RequestMethod.GET)
    public List<Employee> employees(){
        return employeeRepository.findAll();
    }

    @RequestMapping(value="department/{id}", method=RequestMethod.GET)
    public List<Employee> departments(@PathVariable("id") int id){
        return departmentRepository
                .findById(id)
                .get()
                .getEmployees();
    }











}
