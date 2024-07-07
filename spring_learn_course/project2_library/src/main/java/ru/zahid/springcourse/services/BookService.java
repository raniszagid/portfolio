package ru.zahid.springcourse.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zahid.springcourse.model.Book;
import ru.zahid.springcourse.model.Person;
import ru.zahid.springcourse.repositories.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (!sortByYear) {
            return bookRepository.findAll();
        } else {
            return bookRepository.findAll(Sort.by("year"));
        }
    }

    public List<Book> findSplittingByPages(Integer page, Integer booksPerPage,
                                           boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(PageRequest
                    .of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public Book findOne(int number) {
        Optional<Book> foundBook = bookRepository.findById(number);

        return foundBook.orElse(null);
    }

    public Person getBookOwner(int number) {
        Optional<Book> foundBook = bookRepository.findById(number);
        return foundBook.get().getOwner();
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int number, Book updatedBook) {
        Book bookToBeUpdated = bookRepository.findById(number).get();
        updatedBook.setNumber(number);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int number) {
        bookRepository.deleteById(number);
    }

    @Transactional
    public void release(int number) {
        bookRepository.findById(number).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setAssignedAt(null);
                }
        );
    }

    @Transactional
    public void assign(int number, Person newOwner) {
        bookRepository.findById(number).ifPresent(
                book -> {
                    book.setOwner(newOwner);
                    book.setAssignedAt(new Date());
                }
        );
    }

    public List<Book> searchByTitle(String query) {
        return bookRepository.findBooksByTitleStartingWith(query);
    }
}
