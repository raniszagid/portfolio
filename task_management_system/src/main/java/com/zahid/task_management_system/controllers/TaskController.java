package com.zahid.task_management_system.controllers;

import com.zahid.task_management_system.dto.*;
import com.zahid.task_management_system.model.*;
import com.zahid.task_management_system.security.PersonDetails;
import com.zahid.task_management_system.services.*;
import com.zahid.task_management_system.exception.InaccessibleActionException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
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
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid TaskDTO task,
                                             BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        taskService.create(taskService.getTaskFromDTO(task), getCurrentUser());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<Task> getTasks(@RequestParam(value = "executor", required = false) String executor,
                               @RequestParam(value = "author", required = false) String author,
                               @RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "page_size", required = false) Integer pageCapacity) {
        return taskService.find(author, executor, page, pageCapacity);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable("id") int id) {
        return taskService.findOneById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") int id) {
        taskService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editTask(@RequestBody @Valid TaskDTO task,
                                               BindingResult bindingResult,
                                               @PathVariable("id") int id) {
        checkBindingResult(bindingResult);
        taskService.update(id, task);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/change")
    public ResponseEntity<HttpStatus> changePriorityAndStatus(@RequestBody @Valid PriorityStatusDTO data,
                                                              BindingResult bindingResult,
                                                              @PathVariable("id") int id) {
        checkBindingResult(bindingResult);
        taskService.changePriorityStatus(data, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<HttpStatus> changeStatus(@RequestBody @Valid Status status,
                                                   BindingResult bindingResult,
                                                   @PathVariable("id") int id) {
        if (hasLimitedAccess(id)) {
            checkBindingResult(bindingResult);
            taskService.changeStatus(status, id);
            return ResponseEntity.ok(HttpStatus.OK);
        } else throw new InaccessibleActionException();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/assign")
    public ResponseEntity<HttpStatus> assignExecutor(@RequestBody @Valid Executor executor,
                                                     BindingResult bindingResult,
                                                     @PathVariable("id") int id) {
        checkBindingResult(bindingResult);
        taskService.assignExecutor(userService.findUserByName(executor.getUsername()), id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/comment")
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
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
