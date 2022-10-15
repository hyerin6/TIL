package net.skhu.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.skhu.dto.Course;
import net.skhu.mapper.CourseMapper;
import net.skhu.mapper.DepartmentMapper;
import net.skhu.mapper.ProfessorMapper;

@Controller
@RequestMapping("course")
public class CourseController {

    @Autowired CourseMapper courseMapper;
    @Autowired DepartmentMapper departmentMapper;
    @Autowired ProfessorMapper professorMapper;

    @RequestMapping(value="list", method=RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("courses", courseMapper.findAll());
        model.addAttribute("departments", departmentMapper.findAll());
        model.addAttribute("professors", professorMapper.findAll());
        return "course/list";
    }

    @RequestMapping(value="list", method=RequestMethod.POST)
    public String list(Model model,
            @RequestParam("id") int[] id,
            @RequestParam("courseName") String[] courseName,
            @RequestParam("unit") int[] unit,
            @RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date[] startDate,
            @RequestParam("departmentId") int[] departmentId,
            @RequestParam("professorId") int[] professorId)
    {
        save(id, courseName, unit, startDate, departmentId, professorId);
        return list(model);
    }

    @Transactional
    private void save(int[] id, String[] courseName, int[] unit, Date[] startDate,
                      int[] departmentId, int[] professorId)
    {
        for (int i = 0; i < courseName.length; ++i) {
            Course course = new Course();
            course.setId(id[i]);
            course.setCourseName(courseName[i]);
            course.setUnit(unit[i]);
            course.setStartDate(startDate[i]);
            course.setDepartmentId(departmentId[i]);
            course.setProfessorId(professorId[i]);
            courseMapper.update(course);
        }
    }

}
