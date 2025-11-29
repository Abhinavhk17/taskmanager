package com.api.taskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    private String id;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private long fileSize;
    private String uploadedBy;
    private LocalDateTime uploadedAt;
}

