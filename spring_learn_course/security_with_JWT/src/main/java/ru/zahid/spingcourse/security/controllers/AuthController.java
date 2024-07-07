package ru.zahid.spingcourse.security.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zahid.spingcourse.security.dto.AuthenticationDTO;
import ru.zahid.spingcourse.security.dto.PersonDTO;
import ru.zahid.spingcourse.security.model.Person;
import ru.zahid.spingcourse.security.security_class.JWTUtil;
import ru.zahid.spingcourse.security.services.RegistrationService;
import ru.zahid.spingcourse.security.util.PersonValidator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                      BindingResult bindingResult) {
        Person person = convertToPerson(personDTO);
        personValidator.validate(person, bindingResult);
        Map<String, String> returning_map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            returning_map.put("message", "error!!!");
            return returning_map;
        }
        registrationService.register(person);
        String token = jwtUtil.generateToken(person.getUsername());
        returning_map.put("jwt-token", token);
        return returning_map;
    }


    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        Map<String, String> m = new HashMap<>();
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            m.put("message", "incorrect credentials!!!");
            return m;
        }
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        m.put("jwt-token", token);
        return m;
    }


    public Person convertToPerson(PersonDTO personDTO) {
        return this.modelMapper.map(personDTO, Person.class);
    }
}




