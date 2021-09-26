package net.skhu.mapper;

import net.skhu.dto.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> findAll();
    List<Course> findAllWithStudents();
}
