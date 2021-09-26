package net.skhu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String phone;

    String address;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "employeeId")
    Employee employee;

}
