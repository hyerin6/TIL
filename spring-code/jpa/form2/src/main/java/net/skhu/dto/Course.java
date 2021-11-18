package net.skhu.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Course {
    int id;
    String courseName;
    int unit;
    Date startDate;
    int departmentId;
    int professorId;
}
