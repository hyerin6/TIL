package net.skhu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import net.skhu.dto.Course;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CourseMapper {

    @Select("SELECT * FROM Course")
    List<Course> findAll();

    @Update("UPDATE course " +
            "SET courseName = #{courseName}, " +
            "unit = #{unit}, " +
            "startDate = #{startDate}, " +
            "departmentId = #{departmentId}, " +
            "professorId = #{professorId} " +
            "WHERE id = #{id}")
    void update(Course course);

    @Insert("INSERT course (courseName, unit, startDate, departmentId, professorId) " +
            "VALUES (#{courseName}, #{unit}, #{startDate}, #{departmentId}, #{professorId})")
    void insert(Course course);
    
    void delete(int id);

}
