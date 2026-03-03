package org.amancoding.demo.service;

import org.amancoding.demo.dto.TaskRequest;
import org.amancoding.demo.entity.TaskEntity;
import org.amancoding.demo.entity.TaskStatus;
import java.util.List;

public interface TaskService {
    TaskEntity createTask(TaskRequest request, String tenantId, String creatorEmail);

    List<TaskEntity> getTasksByTenant(String tenantId);

    TaskEntity updateTaskStatus(String taskId, TaskStatus status);

    void deleteTask(String taskId);
}