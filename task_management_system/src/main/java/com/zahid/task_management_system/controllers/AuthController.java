package com.zahid.task_management_system.controllers;

import com.zahid.task_management_system.dto.AuthenticationDTO;
import com.zahid.task_management_system.model.User;
import com.zahid.task_management_system.security.JWTUtil;
import com.zahid.task_management_system.services.RegistrationService;
import com.zahid.task_management_system.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserValidator userValidator,
                          RegistrationService registrationService,
                          JWTUtil jwtUtil,
                          AuthenticationManager authenticationManager) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid User user,
                                                   BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        Map<String, String> response = new HashMap<>();
        checkBindingResult(bindingResult);
        registrationService.register(user);
        String token = jwtUtil.generateToken(user.getUsername());
        response.put("jwt-token", token);
        return response;
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
            m.put("error", "incorrect credentials");
            return m;
        }
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        m.put("jwt-token", token);
        return m;
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    private void checkBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append("; ");
            }
            throw new RuntimeException(errorMsg.toString());
        }
    }
}