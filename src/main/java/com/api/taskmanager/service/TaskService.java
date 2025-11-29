package com.api.taskmanager.service;

import com.api.taskmanager.model.Attachment;
import com.api.taskmanager.model.Comment;
import com.api.taskmanager.model.Task;
import com.api.taskmanager.model.User;
import com.api.taskmanager.repository.TaskRepository;
import com.api.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(Task task, String createdBy) {
        task.setCreatedBy(createdBy);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        if (task.getStatus() == null) {
            task.setStatus(Task.TaskStatus.OPEN);
        }
        if (task.getPriority() == null) {
            task.setPriority(Task.TaskPriority.MEDIUM);
        }

        return taskRepository.save(task);
    }

    public Task updateTask(String taskId, Task updatedTask) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (updatedTask.getTitle() != null) {
            task.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) {
            task.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getDueDate() != null) {
            task.setDueDate(updatedTask.getDueDate());
        }
        if (updatedTask.getStatus() != null) {
            task.setStatus(updatedTask.getStatus());
            if (updatedTask.getStatus() == Task.TaskStatus.COMPLETED) {
                task.setCompletedAt(LocalDateTime.now());
            }
        }
        if (updatedTask.getPriority() != null) {
            task.setPriority(updatedTask.getPriority());
        }
        if (updatedTask.getAssignedTo() != null) {
            task.setAssignedTo(updatedTask.getAssignedTo());
        }
        if (updatedTask.getProjectId() != null) {
            task.setProjectId(updatedTask.getProjectId());
        }

        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public void deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
    }

    public Task getTaskById(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByAssignedUser(String userId) {
        return taskRepository.findByAssignedTo(userId);
    }

    public List<Task> getTasksByCreator(String userId) {
        return taskRepository.findByCreatedBy(userId);
    }

    public List<Task> getTasksByProject(String projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getTasksByAssignedUserAndStatus(String userId, Task.TaskStatus status) {
        return taskRepository.findByAssignedToAndStatus(userId, status);
    }

    public List<Task> searchTasks(String keyword) {
        return taskRepository.searchByTitleOrDescription(keyword);
    }

    public Task assignTask(String taskId, String userId) {
        Task task = getTaskById(taskId);

        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignedTo(userId);
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task markTaskAsCompleted(String taskId) {
        Task task = getTaskById(taskId);
        task.setStatus(Task.TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task addComment(String taskId, String userId, String content) {
        Task task = getTaskById(taskId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setUsername(user.getUsername());
        comment.setCreatedAt(LocalDateTime.now());

        task.getComments().add(comment);
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public Task addAttachment(String taskId, Attachment attachment) {
        Task task = getTaskById(taskId);

        attachment.setId(UUID.randomUUID().toString());
        attachment.setUploadedAt(LocalDateTime.now());

        task.getAttachments().add(attachment);
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }
}

