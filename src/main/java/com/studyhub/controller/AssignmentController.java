package com.studyhub.controller;

import com.studyhub.dto.AssignmentRequest;
import com.studyhub.dto.AssignmentResponse;
import com.studyhub.entity.AssignmentStatus;
import com.studyhub.entity.Priority;
import com.studyhub.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<AssignmentResponse> createAssignment(@Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse response = assignmentService.createAssignment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<AssignmentResponse> getAllAssignments(
            @RequestParam(required = false) AssignmentStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long courseId) {

        if (status != null) {
            return assignmentService.getAssignmentsByStatus(status);
        }
        if (priority != null) {
            return assignmentService.getAssignmentsByPriority(priority);
        }
        if (courseId != null) {
            return assignmentService.getAssignmentsByCourse(courseId);
        }
        return assignmentService.getAllAssignments();
    }

    @GetMapping("/{id}")
    public AssignmentResponse getAssignmentById(@PathVariable Long id) {
        return assignmentService.getAssignmentById(id);
    }

    @PutMapping("/{id}")
    public AssignmentResponse updateAssignment(@PathVariable Long id, @Valid @RequestBody AssignmentRequest request) {
        return assignmentService.updateAssignment(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public AssignmentResponse markAsCompleted(@PathVariable Long id) {
        return assignmentService.markAsCompleted(id);
    }

}
