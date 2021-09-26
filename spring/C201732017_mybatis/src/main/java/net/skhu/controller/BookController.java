package net.skhu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.skhu.dto.Book;
import net.skhu.dto.Category;
import net.skhu.dto.Publisher;
import net.skhu.mapper.BookMapper;
import net.skhu.mapper.CategoryMapper;
import net.skhu.mapper.PublisherMapper;
import net.skhu.model.Pagination;

@Controller
@RequestMapping("book")
public class BookController {

    @Autowired BookMapper bookMapper;
    @Autowired CategoryMapper categoryMapper;
    @Autowired PublisherMapper publisherMapper;

    @RequestMapping("list")
    public String list(Pagination pagination, Model model) {
        List<Book> list = bookMapper.findAll(pagination);
        pagination.setRecordCount(bookMapper.count());
        model.addAttribute("list", list);
        return "book/list";
    }

    @RequestMapping(value="create", method=RequestMethod.GET)
    public String create(Model model) {
        Book book = new Book();
        List<Category> categories = categoryMapper.findAll();
        List<Publisher> publishers = publisherMapper.findAll();
        model.addAttribute("book", book);
        model.addAttribute("categories", categories);
        model.addAttribute("publishers", publishers);
        return "book/edit";
    }

    @RequestMapping(value="create", method=RequestMethod.POST)
    public String create(Model model, Book book) {
        bookMapper.insert(book);
        return "redirect:list";
    }

    @RequestMapping(value="edit", method=RequestMethod.GET)
    public String edit(Model model, @RequestParam("id") int id, Pagination pagination) {
        Book book = bookMapper.findById(id);
        List<Category> categories = categoryMapper.findAll();
        List<Publisher> publishers = publisherMapper.findAll();
        model.addAttribute("book", book);
        model.addAttribute("categories", categories);
        model.addAttribute("publishers", publishers);
        return "book/edit";
    }

    @RequestMapping(value="edit", method=RequestMethod.POST)
    public String edit(Model model, Book book, Pagination pagination) {
        bookMapper.update(book);
        return "redirect:list?" + pagination.getQueryString();
    }

    @RequestMapping("delete")
    public String delete(Model model, @RequestParam("id") int id, Pagination pagination) {
        bookMapper.delete(id);
        return "redirect:list?" + pagination.getQueryString();
    }

}
