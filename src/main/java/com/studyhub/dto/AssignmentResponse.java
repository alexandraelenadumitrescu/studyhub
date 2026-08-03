package com.studyhub.dto;

import com.studyhub.entity.AssignmentStatus;
import com.studyhub.entity.Priority;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssignmentResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final AssignmentStatus status;
    private final Priority priority;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Long courseId;
    private final String courseName;

    private AssignmentResponse(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.dueDate = builder.dueDate;
        this.status = builder.status;
        this.priority = builder.priority;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.courseId = builder.courseId;
        this.courseName = builder.courseName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private LocalDate dueDate;
        private AssignmentStatus status;
        private Priority priority;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long courseId;
        private String courseName;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder status(AssignmentStatus status) {
            this.status = status;
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder courseId(Long courseId) {
            this.courseId = courseId;
            return this;
        }

        public Builder courseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public AssignmentResponse build() {
            return new AssignmentResponse(this);
        }
    }

}
