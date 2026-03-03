package org.amancoding.demo.service.Impl;

import org.amancoding.demo.dto.TaskRequest;
import org.amancoding.demo.entity.TaskEntity;
import org.amancoding.demo.entity.TaskStatus;
import org.amancoding.demo.repository.TaskRepository;
import org.amancoding.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    @Transactional
    public TaskEntity createTask(TaskRequest request, String tenantId, String creatorEmail) {
        TaskEntity task = new TaskEntity();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setAssignedTo(request.getAssignedTo());
        task.setDueDate(request.getDueDate());
        task.setTenantId(tenantId); // ✨ Multi-tenancy fix
        task.setCreatedBy(creatorEmail);

        return taskRepository.save(task);
    }

    @Override
    public List<TaskEntity> getTasksByTenant(String tenantId) {
        return taskRepository.findAllByTenantId(tenantId);
    }

    @Override
    @Transactional
    public TaskEntity updateTaskStatus(String taskId, TaskStatus status) {
        TaskEntity task = taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new RuntimeException("Task nahi mila!"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(String taskId) {
        TaskEntity task = taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new RuntimeException("Task nahi mila!"));
        taskRepository.delete(task);
    }
}