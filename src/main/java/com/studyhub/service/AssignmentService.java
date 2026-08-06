package com.studyhub.service;

import com.studyhub.dto.AssignmentRequest;
import com.studyhub.dto.AssignmentResponse;
import com.studyhub.entity.Assignment;
import com.studyhub.entity.AssignmentStatus;
import com.studyhub.entity.Course;
import com.studyhub.entity.Priority;
import com.studyhub.exception.ResourceNotFoundException;
import com.studyhub.repository.AssignmentRepository;
import com.studyhub.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, CourseRepository courseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
    }

    public AssignmentResponse createAssignment(AssignmentRequest request) {
        Course course = findCourseOrThrow(request.getCourseId());

        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(request.getDueDate());
        assignment.setStatus(request.getStatus());
        assignment.setPriority(request.getPriority());
        assignment.setCourse(course);

        Assignment saved = assignmentRepository.save(assignment);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AssignmentResponse getAssignmentById(Long id) {
        return toResponse(findAssignmentOrThrow(id));
    }

    public AssignmentResponse updateAssignment(Long id, AssignmentRequest request) {
        Assignment assignment = findAssignmentOrThrow(id);
        Course course = findCourseOrThrow(request.getCourseId());

        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(request.getDueDate());
        if (request.getStatus() != null) {
            assignment.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            assignment.setPriority(request.getPriority());
        }
        assignment.setCourse(course);

        return toResponse(assignment);
    }

    public void deleteAssignment(Long id) {
        Assignment assignment = findAssignmentOrThrow(id);
        assignmentRepository.delete(assignment);
    }

    public AssignmentResponse markAsCompleted(Long id) {
        Assignment assignment = findAssignmentOrThrow(id);
        assignment.setStatus(AssignmentStatus.COMPLETED);
        return toResponse(assignment);
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignmentsByStatus(AssignmentStatus status) {
        return assignmentRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignmentsByPriority(Priority priority) {
        return assignmentRepository.findByPriority(priority).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignmentsByCourse(Long courseId) {
        findCourseOrThrow(courseId);
        return assignmentRepository.findByCourseId(courseId).stream()
                .map(this::toResponse)
                .toList();
    }

    private Assignment findAssignmentOrThrow(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id " + id));
    }

    private Course findCourseOrThrow(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));
    }

    private AssignmentResponse toResponse(Assignment assignment) {
        return AssignmentResponse.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .dueDate(assignment.getDueDate())
                .status(assignment.getStatus())
                .priority(assignment.getPriority())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .courseId(assignment.getCourse().getId())
                .courseName(assignment.getCourse().getName())
                .build();
    }

}
