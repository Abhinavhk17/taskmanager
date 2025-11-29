package com.api.taskmanager.repository;

import com.api.taskmanager.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findByCreatedBy(String createdBy);
    List<Project> findByMembersContaining(String userId);
    List<Project> findByActiveTrue();
}

