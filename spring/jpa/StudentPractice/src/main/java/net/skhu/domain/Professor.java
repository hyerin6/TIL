package net.skhu.domain;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String professorName;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    Department department;
}
