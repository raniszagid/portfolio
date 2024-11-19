package com.zahid.task_management_system.services;

import com.zahid.task_management_system.model.Comment;
import com.zahid.task_management_system.model.Task;
import com.zahid.task_management_system.model.User;
import com.zahid.task_management_system.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    public void add(Comment comment, User user, Task task) {
        comment.setCommentator(user);
        comment.setTask(task);
        comment.setCommentatorName(user.getUsername());
        commentRepository.save(comment);
    }
}
