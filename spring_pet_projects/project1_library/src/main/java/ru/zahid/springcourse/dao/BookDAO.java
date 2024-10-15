package ru.zahid.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.zahid.springcourse.models.Book;
import ru.zahid.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM books", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int number) {
        return jdbcTemplate
                .query("SELECT * FROM books WHERE number=?", new Object[]{number},
                        new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public Optional<Person> getBookOwner(int number) {
        return jdbcTemplate.query("select person.* from books join person on books.ownerid = person.id where books.number=?",
                new Object[]{number}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO books(bookName, author, publicationYear) VALUES(?,?,?)",
                book.getBookName(), book.getAuthor(), book.getPublicationYear());
    }

    public void update(int number, Book edittedBook) {
        jdbcTemplate.update("UPDATE books SET bookName=?, author=?, publicationYear=?  WHERE number=?",
                edittedBook.getBookName(), edittedBook.getAuthor(), edittedBook.getPublicationYear(),
                number);
    }

    public void delete(int number) {
        jdbcTemplate.update("DELETE FROM books WHERE number=?", number);
    }

    public void release(int number) {
        jdbcTemplate.update("UPDATE books SET ownerid=NULL WHERE number=?", number);
    }

    public void assign(int number, Person selectedPerson) {
        jdbcTemplate.update("UPDATE books SET ownerid=? WHERE number=?", selectedPerson.getId(), number);
    }
}
