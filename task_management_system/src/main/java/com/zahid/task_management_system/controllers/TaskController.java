package com.zahid.task_management_system.controllers;

import com.zahid.task_management_system.dto.*;
import com.zahid.task_management_system.exceptions.TaskNotFoundException;
import com.zahid.task_management_system.exceptions.UserNotFoundException;
import com.zahid.task_management_system.models.*;
import com.zahid.task_management_system.security.PersonDetails;
import com.zahid.task_management_system.services.*;
import com.zahid.task_management_system.exceptions.InaccessibleActionException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(
        name = "Task controller",
        description = "Контроллер для управления задачами"
)
@SecurityRequirement(name = "bearerAuth")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, CommentService commentService) {
        this.taskService = taskService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    @Operation(
            summary = "Создать новую задачу",
            parameters = @Parameter(
                    name = "taskDTO", description = "название, описание, прироритет и статус задачи", required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "запрос выполнен успешно"
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "данное действие доступно только для администраторов"
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "неправильный запрос"
                    ),
            }
    )
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid TaskDTO task,
                                             BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        taskService.create(taskService.getTaskFromDTO(task), getCurrentUser());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "получить все задачи",
            description = "получить все задачи с возможгостью фильтрации по имени автора и/или " +
                    "исполнителя и пагинации",
            parameters = {@Parameter(name = "executor", description = "имя исполнителя задачи"),
                    @Parameter(name = "author", description = "имя автора задачи"),
                    @Parameter(name = "page", description = "номер страницы"),
                    @Parameter(name = "pageSize", description = "количество задач на одной странице")},
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "запрос выполнен успешно",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))
                    )
            }
    )
    public List<Task> getTasks(@RequestParam(value = "executor", required = false) String executor,
                               @RequestParam(value = "author", required = false) String author,
                               @RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "pageSize", required = false) Integer pageCapacity) {
        return taskService.find(author, executor, page, pageCapacity);
    }

    @Operation(
            summary = "получить задачу по её ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "запрос выполнен успешно",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "404", description = "задачи с данным ID не найдено",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class)))
            }
    )
    @GetMapping("/{id}")
    public Task getTask(@PathVariable("id") int id) {
        return taskService.findOneById(id);
    }

    @Operation(
            summary = "удалить задачу по её ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "запрос выполнен успешно"),
                    @ApiResponse(responseCode = "404", description = "задачи с данным ID не найдено"),
                    @ApiResponse(responseCode = "403", description = "данное действие доступно только для администраторов")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") int id) {
        taskService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(
            summary = "обновить задачу по её ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "запрос выполнен успешно"),
                    @ApiResponse(responseCode = "404", description = "задачи с данным ID не найдено"),
                    @ApiResponse(responseCode = "403", description = "данное действие доступно только для администраторов")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editTask(@RequestBody @Valid TaskDTO task,
                                               BindingResult bindingResult,
                                               @PathVariable("id") int id) {
        checkBindingResult(bindingResult);
        taskService.update(id, task);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(
            summary = "поменять статус и/или приоритет задачи с заданным ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "запрос выполнен успешно"),
                    @ApiResponse(responseCode = "404", description = "задачи с данным ID не найдено"),
                    @ApiResponse(responseCode = "403", description = "данное действие доступно только для администраторов")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/priority_status")
    public ResponseEntity<HttpStatus> changePriorityAndStatus(@RequestBody @Valid PriorityStatusDTO data,
                                                              BindingResult bindingResult,
                                                              @PathVariable("id") int id) {
        checkBindingResult(bindingResult);
        taskService.changePriorityStatus(data, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @Operation(
            summary = "поменять статус и/или приоритет задачи с заданным ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "запрос выполнен успешно"),
                    @ApiResponse(responseCode = "404", description = "задачи с данным ID не найдено"),
                    @ApiResponse(responseCode = "403", description = "данное действие доступно только для администраторов и исполнителя задачи")
            }
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<HttpStatus> changeStatus(@RequestBody @Valid Status status,
                                                   BindingResult bindingResult,
                                                   @PathVariable("id") int id) {
        if (hasLimitedAccess(id)) {
            checkBindingResult(bindingResult);
            taskService.changeStatus(status, id);
            return ResponseEntity.ok(HttpStatus.OK);
        } else throw new InaccessibleActionException();
    }

    @Operation(
            summary = "поменять статус и/или приоритет задачи с заданным ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "запрос выполнен успешно"),
                    @ApiResponse(responseCode = "404", description = "задачи с данным ID либо пользователя с данным именем не найдено"),
                    @ApiResponse(responseCode = "403", description = "данное действие доступно только для администраторов и исполнителя задачи")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/executor")
    public ResponseEntity<HttpStatus> assignExecutor(@RequestBody @Valid Executor executor,
                                                     BindingResult bindingResult,
                                                     @PathVariable("id") int id) {
        checkBindingResult(bindingResult);
        taskService.assignExecutor(userService.findUserByName(executor.getUsername()), id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(
            summary = "поменять статус и/или приоритет задачи с заданным ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "запрос выполнен успешно"),
                    @ApiResponse(responseCode = "404", description = "задачи с данным ID не найдено"),
                    @ApiResponse(responseCode = "403", description = "данное действие доступно только для администраторов и исполнителя задачи")
            }
    )
    @PostMapping("/{id}/comment")
    public ResponseEntity<HttpStatus> addComment(@RequestBody @Valid Comment comment,
                                                 BindingResult bindingResult,
                                                 @PathVariable("id") int id) {
        if (hasLimitedAccess(id)) {
            checkBindingResult(bindingResult);
            Task task = taskService.findOneById(id);
            commentService.add(comment, getCurrentUser(), task);
            task.addComment(comment);
            taskService.save(task);
            return ResponseEntity.ok(HttpStatus.OK);
        } else throw new InaccessibleActionException();
    }
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String username = personDetails.getUsername();
        return userService.findUserByName(username);
    }
    protected boolean hasLimitedAccess(int id) {
        User user = getCurrentUser();
        Task task = taskService.findOneById(id);
        return user.equals(task.getExecutor()) || user.getRole().equals("ROLE_ADMIN");
    }
    protected void checkBindingResult(BindingResult bindingResult) {
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

    @ExceptionHandler
    private ResponseEntity<String> handleException(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (e instanceof TaskNotFoundException || e instanceof UserNotFoundException)
            status = HttpStatus.NOT_FOUND;
        else if (e instanceof InaccessibleActionException || e instanceof AuthorizationDeniedException)
            status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(e.getMessage(), status);
    }
}
