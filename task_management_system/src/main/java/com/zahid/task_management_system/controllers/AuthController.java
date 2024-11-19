package com.zahid.task_management_system.controllers;

import com.zahid.task_management_system.dto.AuthenticationDTO;
import com.zahid.task_management_system.models.User;
import com.zahid.task_management_system.security.JWTUtil;
import com.zahid.task_management_system.services.RegistrationService;
import com.zahid.task_management_system.util.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication controller",
        description = "контроллер для аутентификации пользователя"
)
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

    @Operation(
            summary = "Регистрация пользователя",
            description = "Регистрация пользователя путём ввода логина в формате электронной почты " +
                    "и пароля. По умолчанию первый зарегистрировавшийся пользователь получает статус " +
                    "администратора, остальные получают статус обычных пользователей - " +
                    "для простоты тестирования",
            parameters = @Parameter(
                    name = "authenticationDTO", description = "логин-пароль пользователя", required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "пользователь зарегистрирован, ему выдается JWT-токен",
                            content =  @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "недопустимые значения",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
                    )
            }
    )
    @PostMapping("/registration")
    public ResponseEntity<String> performRegistration(@RequestBody @Valid AuthenticationDTO authenticationDTO,
                                                   BindingResult bindingResult) {
        User user = new User(authenticationDTO.getUsername(), authenticationDTO.getPassword());
        userValidator.validate(user, bindingResult);
        checkBindingResult(bindingResult);
        registrationService.register(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @Operation(
            description = "Вход в учетную запись пользователя",
            summary = "Вход в систему пользователем с предоставлением его данных - логин и пароль",
            parameters = @Parameter(
                    name = "authenticationDTO", description = "логин-пароль пользователя", required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "пользователь вошел в систему, ему выдается JWT-токен",
                            content =  @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "401", description = "неверные логин или пароль",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect credentials", HttpStatus.UNAUTHORIZED);
        }
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return new ResponseEntity<>(token, HttpStatus.OK);
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