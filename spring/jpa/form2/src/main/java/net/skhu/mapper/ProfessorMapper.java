package net.skhu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.skhu.dto.Professor;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProfessorMapper {

    @Select("SELECT * FROM Professor")
    List<Professor> findAll();

}
