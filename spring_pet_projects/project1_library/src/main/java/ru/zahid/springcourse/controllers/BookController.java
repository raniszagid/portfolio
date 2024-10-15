package ru.zahid.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zahid.springcourse.dao.BookDAO;
import ru.zahid.springcourse.dao.PersonDAO;
import ru.zahid.springcourse.models.Book;
import ru.zahid.springcourse.models.Person;
import ru.zahid.springcourse.util.BookValidator;
import ru.zahid.springcourse.util.PersonValidator;

import java.util.Optional;

@Controller
@RequestMapping("/bookshelf")
public class BookController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final BookValidator bookValidator;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO, BookValidator bookValidator) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.bookValidator = bookValidator;
    }
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("bookshelf", bookDAO.index());
        return "bookshelf/index";
    }

    @GetMapping("/{number}")
    public String show(@PathVariable("number") int number, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookDAO.show(number));
        Optional<Person> owner = bookDAO.getBookOwner(number);
        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
        } else {
            model.addAttribute("people", personDAO.index());
        }
        return "bookshelf/show";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "bookshelf/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "bookshelf/new";
        bookDAO.save(book);
        return "redirect:/bookshelf";
    }

    @GetMapping("/{number}/edit")
    public String edit(Model model, @PathVariable("number") int number) {
        model.addAttribute("book", bookDAO.show(number));
        return "bookshelf/edit";
    }

    @PatchMapping("/{number}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult, @PathVariable("number") int number) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) return "bookshelf/edit";
        bookDAO.update(number, book);
        return "redirect:/bookshelf";
    }

    @DeleteMapping("/{number}")
    public String delete(@PathVariable("number") int number) {
        bookDAO.delete(number);
        return "redirect:/bookshelf";
    }

    @PatchMapping("/{number}/release")
    public String release(@PathVariable("number") int number) {
        bookDAO.release(number);
        return "redirect:/bookshelf/" + number;
    }

    @PatchMapping("/{number}/assign")
    public String assign(@PathVariable("number") int number, @ModelAttribute("person") @Valid Person person) {
        bookDAO.assign(number, person);
        return "redirect:/bookshelf/" + number;
    }
}
