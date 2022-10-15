package net.skhu.controller;

import net.skhu.domain.Book;
import net.skhu.domain.Category;
import net.skhu.domain.Publisher;
import net.skhu.model.Pagination;
import net.skhu.repository.BookRepository;
import net.skhu.repository.CategoryRepository;
import net.skhu.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("book")
public class APIController {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    PublisherRepository publisherRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @RequestMapping("list")
    public String list(Pagination pagination, Model model) {
        List<Book> list = bookRepository.findAll(pagination);
        model.addAttribute("list", list);
        return "book/list";
    }

    @RequestMapping(value="create", method= RequestMethod.GET)
    public String create(Model model, Book book){
        List<Publisher> publishers = publisherRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("publishers", publishers);
        model.addAttribute("categories", categories);
        model.addAttribute("book", book);
        return "book/edit";
    }

    @RequestMapping(value="create", method=RequestMethod.POST)
    public String create(Book book) {
        /*
        book의 categoryId 필드는 (존재하지 않는 카테고리로 변경할 수 있는 상황을 막기 위함)
        insert와 update할 때 사용할 수 없게 설정
        -> book을 insert, category를 update 할 경우 category를 findById로 book에 넣어줘야 한다.
         */
        Category category = categoryRepository.findById(book.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid categoryId"));
        Publisher publisher = publisherRepository.findById(book.getPublisherId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid publisherId"));
        book.setCategory(category);
        book.setPublisher(publisher);
        bookRepository.save(book);
        return "redirect:list";
    }

    @RequestMapping(value="edit", method=RequestMethod.GET)
    public String edit(Model model, @RequestParam("id") int id, Pagination pagination) {
        Optional<Book> book = bookRepository.findById(id);
        model.addAttribute("book", book.get());
        List<Publisher> publishers = publisherRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("publishers", publishers);
        model.addAttribute("categories", categories);
        return "book/edit";
    }

    @RequestMapping(value="edit", method=RequestMethod.POST)
    public String edit(Model model, Book book, Pagination pagination) {
        Category category = categoryRepository.findById(book.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid categoryId"));
        Publisher publisher = publisherRepository.findById(book.getPublisherId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid publisherId"));
        book.setCategory(category);
        book.setPublisher(publisher);
        bookRepository.save(book);
        return "redirect:list?" + pagination.getQueryString();
    }

    @RequestMapping("delete")
    public String delete(Model model, @RequestParam("id") int id, Pagination pagination) {
        bookRepository.deleteById(id);
        return "redirect:list?" + pagination.getQueryString();
    }

}
