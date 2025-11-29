package com.api.taskmanager.service;

import com.api.taskmanager.model.Project;
import com.api.taskmanager.repository.ProjectRepository;
import com.api.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public Project createProject(Project project, String createdBy) {
        project.setCreatedBy(createdBy);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        // Add creator as a member
        if (!project.getMembers().contains(createdBy)) {
            project.getMembers().add(createdBy);
        }

        return projectRepository.save(project);
    }

    public Project updateProject(String projectId, Project updatedProject) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (updatedProject.getName() != null) {
            project.setName(updatedProject.getName());
        }
        if (updatedProject.getDescription() != null) {
            project.setDescription(updatedProject.getDescription());
        }

        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public void deleteProject(String projectId) {
        projectRepository.deleteById(projectId);
    }

    public Project getProjectById(String projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsByCreator(String userId) {
        return projectRepository.findByCreatedBy(userId);
    }

    public List<Project> getProjectsByMember(String userId) {
        return projectRepository.findByMembersContaining(userId);
    }

    public Project addMember(String projectId, String userId) {
        Project project = getProjectById(projectId);

        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!project.getMembers().contains(userId)) {
            project.getMembers().add(userId);
            project.setUpdatedAt(LocalDateTime.now());
            return projectRepository.save(project);
        }

        return project;
    }

    public Project removeMember(String projectId, String userId) {
        Project project = getProjectById(projectId);

        project.getMembers().remove(userId);
        project.setUpdatedAt(LocalDateTime.now());

        return projectRepository.save(project);
    }
}

