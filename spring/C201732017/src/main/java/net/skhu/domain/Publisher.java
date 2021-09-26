package net.skhu.domain;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String title;
}
