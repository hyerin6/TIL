package net.skhu.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    @ManyToOne
    @JoinColumn (name = "departmentId")
    Department department;

    @OneToOne(mappedBy="employee")
    Address address;

}
