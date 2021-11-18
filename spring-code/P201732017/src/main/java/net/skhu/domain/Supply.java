package net.skhu.domain;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "productId")
    Product product;

    @ManyToOne
    @JoinColumn(name = "dealerId")
    Dealer dealer;

}
