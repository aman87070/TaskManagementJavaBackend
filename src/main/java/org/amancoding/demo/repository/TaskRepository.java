package org.amancoding.demo.repository;

import org.amancoding.demo.entity.TaskEntity;
import org.amancoding.demo.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    // 1. ✨ Ek specific tenant ke saare tasks dhoondhne ke liye
    List<TaskEntity> findAllByTenantId(String tenantId);

    // 2. Tenant ke andar specific user ko assigned tasks
    List<TaskEntity> findAllByTenantIdAndAssignedTo(String tenantId, String username);

    // 3. Status ke basis par tasks filter karne ke liye (e.g. Done tasks)
    List<TaskEntity> findAllByTenantIdAndStatus(String tenantId, TaskStatus status);

    Optional<TaskEntity> findByTaskId(String taskId);
}