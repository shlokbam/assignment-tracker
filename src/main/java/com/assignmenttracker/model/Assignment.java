package com.assignmenttracker.model;

import java.time.LocalDateTime;

public class Assignment {
    public enum Status {
        NOT_STARTED, IN_PROGRESS, COMPLETED
    }

    private int id;
    private int userId;
    private String title;
    private String courseName;
    private LocalDateTime dueDate;
    private Status status;

    public Assignment(int id, int userId, String title, String courseName,
                      LocalDateTime dueDate, Status status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.courseName = courseName;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}