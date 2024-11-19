package com.zahid.task_management_system.dto;

import com.zahid.task_management_system.models.Priority;
import com.zahid.task_management_system.models.Status;

public class PriorityStatusDTO {
    private Priority priority;
    private Status status;

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}