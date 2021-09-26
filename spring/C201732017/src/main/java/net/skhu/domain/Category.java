package net.skhu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.List;


@Data
@ToString(exclude={"books"})
@EqualsAndHashCode(exclude={"books"})
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String categoryName;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    List<Book> books;

}
