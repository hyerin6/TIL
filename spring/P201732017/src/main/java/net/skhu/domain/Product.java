package net.skhu.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String title;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    Category category;

    int unitCost;

    int quantity;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    List<Supply> supplies;

}
