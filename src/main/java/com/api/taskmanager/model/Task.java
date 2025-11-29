package com.api.taskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;

    private String title;
    private String description;
    private LocalDateTime dueDate;
    
    private TaskStatus status = TaskStatus.OPEN;
    private TaskPriority priority = TaskPriority.MEDIUM;
    
    private String createdBy;
    private String assignedTo;
    private String projectId;
    
    private List<Comment> comments = new ArrayList<>();
    private List<Attachment> attachments = new ArrayList<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;

    public enum TaskStatus {
        OPEN, IN_PROGRESS, COMPLETED, CANCELLED
    }

    public enum TaskPriority {
        LOW, MEDIUM, HIGH, URGENT
    }
}

