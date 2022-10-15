package net.skhu.domain;

import lombok.Data;
import org.hibernate.annotations.Generated;

import javax.persistence.*;

@Entity
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String studentNumber;

    String name;

    int year;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    Department department;

}
