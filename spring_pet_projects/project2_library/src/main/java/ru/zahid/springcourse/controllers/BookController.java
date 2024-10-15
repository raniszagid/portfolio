package ru.zahid.springcourse.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zahid.springcourse.model.Book;
import ru.zahid.springcourse.model.Person;
import ru.zahid.springcourse.repositories.BookRepository;
import ru.zahid.springcourse.services.BookService;
import ru.zahid.springcourse.services.PeopleService;

import java.util.Optional;

@Controller
@RequestMapping("/bookshelf")
public class BookController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage) {
        if (page == null || booksPerPage == null) {
            model.addAttribute("books", bookService.findAll(sortByYear));
        } else {
            model.addAttribute("books", bookService.findSplittingByPages(page-1, booksPerPage, sortByYear));
        }
        return "bookshelf/index";
    }

    @GetMapping("/{number}")
    public String show(@PathVariable("number") int number,
                       Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findOne(number));
        Person owner = bookService.getBookOwner(number);
        if (owner != null) {
            model.addAttribute("owner", owner);
        } else {
            model.addAttribute("people", peopleService.findAll());
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
        if (bindingResult.hasErrors())
            return "bookshelf/new";
        bookService.save(book);
        return "redirect:/bookshelf";
    }

    @GetMapping("/{number}/edit")
    public String edit(Model model, @PathVariable("number") int number) {
        model.addAttribute("book", bookService.findOne(number));
        return "bookshelf/edit";
    }

    @PatchMapping("/{number}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult, @PathVariable("number") int number) {
        if (bindingResult.hasErrors())
            return "bookshelf/edit";

        bookService.update(number, book);
        return "redirect:/bookshelf";
    }

    @DeleteMapping("/{number}")
    public String delete(@PathVariable("number") int number) {
        bookService.delete(number);
        return "redirect:/bookshelf";
    }

    @PatchMapping("/{number}/release")
    public String release(@PathVariable("number") int number) {
        bookService.release(number);
        return "redirect:/bookshelf/" + number;
    }

    @PatchMapping("/{number}/assign")
    public String assign(@PathVariable("number") int number,
                         @ModelAttribute("person") Person person) {
        bookService.assign(number, person);
        return "redirect:/bookshelf/" + number;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "bookshelf/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", bookService.searchByTitle(query));
        return "bookshelf/search";
    }
}
