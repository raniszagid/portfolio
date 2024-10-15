package ru.zahid.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.zahid.springcourse.models.Book;
import ru.zahid.springcourse.models.Person;
import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate
                .query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

/*    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE email=?", new Object[] {email},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }*/

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name, birthYear) VALUES(?,?)",
                person.getName(), person.getBirthYear());
    }

    public void update(int id, Person updatedPer) {
        jdbcTemplate.update("UPDATE Person SET name=?, birthYear=?  WHERE id=?",
                updatedPer.getName(), updatedPer.getBirthYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }
    public void addBook(int id, Book givenBook) {
        jdbcTemplate.update("UPDATE books SET ownerid=? WHERE number=?", id, givenBook.getNumber());
    }
    public List<Book> showBookList(int id) {
        return jdbcTemplate.query("SELECT * FROM books WHERE ownerid=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
