package com.zahid.task_management_system.util;

import com.zahid.task_management_system.models.User;
import com.zahid.task_management_system.services.TaskUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final TaskUserDetailsService userDetailsService;

    @Autowired
    public UserValidator(TaskUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (!userDetailsService.check(user.getUsername())) {
            errors.rejectValue("username", "",
                    "Person with current username already exists");
        }
    }
}