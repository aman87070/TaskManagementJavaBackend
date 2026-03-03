package org.amancoding.demo.controller;

import org.amancoding.demo.dto.TaskRequest;
import org.amancoding.demo.entity.TaskEntity;
import org.amancoding.demo.entity.TaskStatus;
import org.amancoding.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin("*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // 1. Create Task
    @PostMapping("/{tenantId}")
    public ResponseEntity<TaskEntity> createTask(
            @PathVariable String tenantId,
            Authentication authentication,
            @RequestBody TaskRequest request) {
        String creatorEmail = authentication.getName();
        return ResponseEntity.ok(taskService.createTask(request, tenantId, creatorEmail));
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<List<TaskEntity>> getTasks(@PathVariable String tenantId) {
        return ResponseEntity.ok(taskService.getTasksByTenant(tenantId));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskEntity> updateStatus(
            @PathVariable String taskId,
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, status));
    }
}