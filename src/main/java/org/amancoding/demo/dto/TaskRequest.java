package org.amancoding.demo.dto;

import org.amancoding.demo.entity.TaskPriority;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskRequest {
    private String title;
    private String description;
    private TaskPriority priority;
    private String assignedTo;

    private LocalDate dueDate;
}