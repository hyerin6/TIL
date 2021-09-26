package net.skhu.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String courseName;

    int unit;

    Date startDate;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    Department department;

    @ManyToOne
    @JoinColumn(name = "professorId")
    Professor professor;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    List<Register> registerList;

}
