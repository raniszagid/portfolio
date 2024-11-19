package com.zahid.task_management_system.services;

import com.zahid.task_management_system.dto.PriorityStatusDTO;
import com.zahid.task_management_system.dto.TaskDTO;
import com.zahid.task_management_system.model.Status;
import com.zahid.task_management_system.model.Task;
import com.zahid.task_management_system.model.User;
import com.zahid.task_management_system.repositories.TaskRepository;
import com.zahid.task_management_system.util.Paginator;
import com.zahid.task_management_system.exception.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Transactional
    public void save(Task task) {
        taskRepository.save(task);
    }
    public List<Task> find(String authorName, String executorName, Integer page, Integer capacity) {
        List<Task> list = taskRepository.findAll();
        if (executorName != null) {
            list = list.stream().filter(t -> t.getExecutor() != null)
                    .filter(t -> t.getExecutorName().equals(executorName)).toList();
        }
        if (authorName != null) {
            list = list.stream()
                    .filter(t -> t.getAuthorName().equals(authorName)).toList();
        }
        if (page != null && capacity != null) {
            list = Paginator.paginate(list, page, capacity);
        }
        return list;
    }
    public Task findOneById(int id) {
        return taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    public void create(Task task, User user) {
        task.setAuthor(user);
        task.setAuthorName(user.getUsername());
        taskRepository.save(task);
    }
    public void delete(int id) {
        taskRepository.deleteById(id);
    }

    public void update(int id, TaskDTO newTask) {
        Task oldTask = findOneById(id);
        if (newTask.getTitle() != null) oldTask.setTitle(newTask.getTitle());
        if (newTask.getDescription() != null)
            oldTask.setDescription(newTask.getDescription());
        if (newTask.getPriority() != null) oldTask.setPriority(newTask.getPriority());
        if (newTask.getStatus() != null) oldTask.setStatus(newTask.getStatus());
        taskRepository.save(oldTask);
    }

    public void changeStatus(Status status, int id) {
        Task task = findOneById(id);
        task.setStatus(status);
        taskRepository.save(task);
    }

    public void changePriorityStatus(PriorityStatusDTO data, int id) {
        Task task = findOneById(id);
        if (data.getPriority() != null) {
            task.setPriority(data.getPriority());
        } else System.out.println("empty priority");
        if (data.getStatus() != null) {
            task.setStatus(data.getStatus());
        } else System.out.println("empty status");
        taskRepository.save(task);
    }

    public void assignExecutor(User executor, int id) {
        Task task = findOneById(id);
        task.setExecutor(executor);
        task.setExecutorName(executor.getUsername());
        taskRepository.save(task);
    }

    public Task getTaskFromDTO(TaskDTO dto) {
        return new Task(dto.getTitle(),dto.getDescription(), dto.getStatus(),
                dto.getPriority());
    }
}
