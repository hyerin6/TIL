package net.skhu.controller;

import java.util.List;

import net.skhu.dto.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.skhu.dto.Student;
import net.skhu.mapper.DepartmentMapper;
import net.skhu.mapper.StudentMapper;

@Controller
@RequestMapping("mybatis")
public class MybatisController {

    @Autowired DepartmentMapper departmentMapper;
    @Autowired StudentMapper studentMapper;

    @RequestMapping(value="cacheTest", method=RequestMethod.GET)
    public String cacheTest(Model model) {
        List<Department> departments = departmentMapper.findAll();
        model.addAttribute("departments", departments);
        model.addAttribute("department", departments.get(0));
        model.addAttribute("students", studentMapper.findAll().subList(0, 5));
        return "mybatis/cacheTest";
    }

    @RequestMapping(value="cacheTest", method=RequestMethod.POST)
    public String cache(Model model, Department department) {
        departmentMapper.update(department);
        return "redirect:cacheTest";
    }


    // departmentList1 액션 메소드는 학과별 학생 목록을 따로 조회해서 Department 객체에 채우고 있다.
    @RequestMapping("departmentList1")
    public String departmentList1(Model model) {
        // 학과 목록을 조회한다.
        List<Department> departments = departmentMapper.findAll();

        // departments 학과 목록의 학과 각각에 대해서 for 루프 본문을 반복 실행한다.
        for (Department department : departments) {
            // 학과 목록의 학과 각각에 대해서, 그 학과에 속한 학생 목록을 조회한다.
            List<Student> students = studentMapper.findByDepartmentId(department.getId());
            // 그 학과에 속한 학생 목록을 그 학과(Department) 객체의 students 멤버 변수에 채운다.
            department.setStudents(students);
        }
        // 학과 목록 객체를 모델에 채운다. - 그 학과에 속한 학생 목록도 채워져 있다.
        model.addAttribute("departments", departments);
        return "mybatis/departmentList";
    }

    // departmentList2 액션 메소드는 학과별 학생 목록을 한 번에 조회한다.
    @RequestMapping("departmentList2")
    public String departmentList2(Model model) {
        model.addAttribute("departments", departmentMapper.findAllWithStudents());
        return "mybatis/departmentList";
    }

}
