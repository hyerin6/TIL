package net.skhu.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String title;

    String author;

    int price;

    @Column(insertable = false, updatable = false)
    int categoryId;

    @Column(insertable = false, updatable = false)
    int publisherId;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    Category category;

    @ManyToOne
    @JoinColumn(name = "publisherId")
    Publisher publisher;

    int available;

}
