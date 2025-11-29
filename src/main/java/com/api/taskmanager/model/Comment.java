package com.api.taskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private String id;
    private String content;
    private String userId;
    private String username;
    private LocalDateTime createdAt;
}

