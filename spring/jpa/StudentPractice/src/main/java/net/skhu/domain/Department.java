package net.skhu.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String departmentName;

    @JsonIgnore
    @OneToMany(mappedBy = "department")
    List<Professor> professorList;

}
