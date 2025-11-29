package com.api.taskmanager.controller;

import com.api.taskmanager.dto.CommentRequest;
import com.api.taskmanager.dto.MessageResponse;
import com.api.taskmanager.dto.TaskRequest;
import com.api.taskmanager.model.Attachment;
import com.api.taskmanager.model.Task;
import com.api.taskmanager.security.UserDetailsImpl;
import com.api.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaskController {

    @Autowired
    private TaskService taskService;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskRequest taskRequest,
                                        Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setStatus(taskRequest.getStatus());
        task.setPriority(taskRequest.getPriority());
        task.setAssignedTo(taskRequest.getAssignedTo());
        task.setProjectId(taskRequest.getProjectId());

        Task createdTask = taskService.createTask(task, userDetails.getId());
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable String id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<?> getMyTasks(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Task> tasks = taskService.getTasksByAssignedUser(userDetails.getId());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/created-by-me")
    public ResponseEntity<?> getTasksCreatedByMe(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Task> tasks = taskService.getTasksByCreator(userDetails.getId());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getTasksByProject(@PathVariable String projectId) {
        List<Task> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable Task.TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/my-tasks/status/{status}")
    public ResponseEntity<?> getMyTasksByStatus(@PathVariable Task.TaskStatus status,
                                                 Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Task> tasks = taskService.getTasksByAssignedUserAndStatus(userDetails.getId(), status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTasks(@RequestParam String keyword) {
        List<Task> tasks = taskService.searchTasks(keyword);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id,
                                        @Valid @RequestBody TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setStatus(taskRequest.getStatus());
        task.setPriority(taskRequest.getPriority());
        task.setAssignedTo(taskRequest.getAssignedTo());
        task.setProjectId(taskRequest.getProjectId());

        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(new MessageResponse("Task deleted successfully!"));
    }

    @PutMapping("/{id}/assign/{userId}")
    public ResponseEntity<?> assignTask(@PathVariable String id,
                                        @PathVariable String userId) {
        Task task = taskService.assignTask(id, userId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> markTaskAsCompleted(@PathVariable String id) {
        Task task = taskService.markTaskAsCompleted(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable String id,
                                        @Valid @RequestBody CommentRequest commentRequest,
                                        Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Task task = taskService.addComment(id, userDetails.getId(), commentRequest.getContent());
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{id}/attachments")
    public ResponseEntity<?> addAttachment(@PathVariable String id,
                                           @RequestParam("file") MultipartFile file,
                                           Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        try {
            // Create upload directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            Path filePath = Paths.get(UPLOAD_DIR + uniqueFilename);

            // Save file
            Files.write(filePath, file.getBytes());

            // Create attachment object
            Attachment attachment = new Attachment();
            attachment.setFileName(originalFilename);
            attachment.setFileType(file.getContentType());
            attachment.setFileUrl(UPLOAD_DIR + uniqueFilename);
            attachment.setFileSize(file.getSize());
            attachment.setUploadedBy(userDetails.getId());

            Task task = taskService.addAttachment(id, attachment);
            return ResponseEntity.ok(task);

        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error uploading file: " + e.getMessage()));
        }
    }
}

