package ru.zahid.springcourse.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zahid.springcourse.model.Book;
import ru.zahid.springcourse.model.Person;
import ru.zahid.springcourse.repositories.BookRepository;
import ru.zahid.springcourse.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> showBookList(int id) {
        Optional<Person> human = peopleRepository.findById(id);
        if (human.isPresent()) {
            Hibernate.initialize(human.get().getBooks());
            human.get().getBooks().forEach( book -> {
                long difference = Math.abs(book.getAssignedAt().getTime() - new Date().getTime());
                // 4 days = 345600000 millis
                if (difference > /*345600000*/ 10000) {
                    book.setDelay(true);
                }
            }
            );
            return human.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }
}