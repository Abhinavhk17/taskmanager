package com.api.taskmanager.repository;

import com.api.taskmanager.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByCreatedBy(String createdBy);
    List<Task> findByAssignedTo(String assignedTo);
    List<Task> findByProjectId(String projectId);
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByAssignedToAndStatus(String assignedTo, Task.TaskStatus status);

    @Query("{ '$or': [ { 'title': { '$regex': ?0, '$options': 'i' } }, { 'description': { '$regex': ?0, '$options': 'i' } } ] }")
    List<Task> searchByTitleOrDescription(String keyword);
}

