package org.example;

import java.time.LocalDateTime;

public final class Task {
    private int id;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;// nullable

    Task(int _id, String _description) {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;

        this.id = _id;
        this.description = _description;

        this.status = "todo";
    }

    public void setStatus(String status) {
        switch(status) {
            case "todo":
            case "in-progress":
            case "done":
                this.status = status;
                break;
            default:
                IO.println("Error: invalid status");
        }
    }
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
        updateTime();
    }

    private void updateTime() {
        this.updatedAt = LocalDateTime.now();
    }
}