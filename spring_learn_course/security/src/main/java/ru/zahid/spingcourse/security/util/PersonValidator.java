package ru.zahid.spingcourse.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zahid.spingcourse.security.model.Person;
import ru.zahid.spingcourse.security.services.PersonDetailService;

@Component
public class PersonValidator implements Validator {
    private final PersonDetailService personDetailService;

    @Autowired
    public PersonValidator(PersonDetailService personDetailService) {
        this.personDetailService = personDetailService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (!personDetailService.check(person.getUsername())) {
            errors.rejectValue("username", "",
                    "Person with current username already exists");
        }
    }
}
