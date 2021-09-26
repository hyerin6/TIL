package net.skhu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.skhu.dto.Book;
import net.skhu.model.Pagination;

@Mapper
public interface BookMapper {

    Book findById(int id);
    List<Book> findAll(Pagination pagination);
    int count();
    void update(Book book);
    void insert(Book book);
    void delete(int id);
}
