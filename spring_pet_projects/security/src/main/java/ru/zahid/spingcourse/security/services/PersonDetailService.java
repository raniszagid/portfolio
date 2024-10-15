package ru.zahid.spingcourse.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.zahid.spingcourse.security.model.Person;
import ru.zahid.spingcourse.security.repositories.PeopleRepository;
import ru.zahid.spingcourse.security.security_class.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(s);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(person.get());
    }

    public boolean check(String s) {
        Optional<Person> person = peopleRepository.findByUsername(s);
        return person.isEmpty();
    }
}
