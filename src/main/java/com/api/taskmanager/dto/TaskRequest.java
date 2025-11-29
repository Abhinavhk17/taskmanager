package com.api.taskmanager.dto;

import com.api.taskmanager.model.Task;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private LocalDateTime dueDate;
    private Task.TaskStatus status;
    private Task.TaskPriority priority;
    private String assignedTo;
    private String projectId;
}

