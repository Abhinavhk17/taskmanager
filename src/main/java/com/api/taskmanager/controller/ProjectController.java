package com.api.taskmanager.controller;

import com.api.taskmanager.dto.MessageResponse;
import com.api.taskmanager.dto.ProjectRequest;
import com.api.taskmanager.model.Project;
import com.api.taskmanager.security.UserDetailsImpl;
import com.api.taskmanager.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectRequest projectRequest,
                                           Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Project project = new Project();
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());

        if (projectRequest.getMembers() != null) {
            project.setMembers(projectRequest.getMembers());
        }

        Project createdProject = projectService.createProject(project, userDetails.getId());
        return ResponseEntity.ok(createdProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable String id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/my-projects")
    public ResponseEntity<?> getMyProjects(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Project> projects = projectService.getProjectsByMember(userDetails.getId());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/created-by-me")
    public ResponseEntity<?> getProjectsCreatedByMe(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Project> projects = projectService.getProjectsByCreator(userDetails.getId());
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable String id,
                                           @Valid @RequestBody ProjectRequest projectRequest) {
        Project project = new Project();
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());

        Project updatedProject = projectService.updateProject(id, project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(new MessageResponse("Project deleted successfully!"));
    }

    @PostMapping("/{id}/members/{userId}")
    public ResponseEntity<?> addMember(@PathVariable String id, @PathVariable String userId) {
        Project project = projectService.addMember(id, userId);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<?> removeMember(@PathVariable String id, @PathVariable String userId) {
        Project project = projectService.removeMember(id, userId);
        return ResponseEntity.ok(project);
    }
}

