package net.skhu.domain;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "studentId")
    Student student;

    @ManyToOne
    @JoinColumn(name = "courseId")
    Course course;

    int grade;

    Date createDate;


}
