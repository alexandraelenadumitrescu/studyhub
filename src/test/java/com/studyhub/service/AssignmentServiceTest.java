package com.studyhub.service;

import com.studyhub.dto.AssignmentRequest;
import com.studyhub.dto.AssignmentResponse;
import com.studyhub.dto.CourseRequest;
import com.studyhub.dto.CourseResponse;
import com.studyhub.entity.AssignmentStatus;
import com.studyhub.entity.Priority;
import com.studyhub.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AssignmentServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AssignmentService assignmentService;

    private Long courseId;

    @BeforeEach
    void setUp() {
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName("Algorithms");
        courseRequest.setCode("CS301-" + System.nanoTime());
        courseRequest.setProfessor("Dr. Pop");
        courseRequest.setSemester("2026-1");
        CourseResponse course = courseService.createCourse(courseRequest);
        this.courseId = course.getId();
    }

    private AssignmentRequest sampleRequest(String title) {
        AssignmentRequest request = new AssignmentRequest();
        request.setTitle(title);
        request.setDescription("Implement and analyze the assignment");
        request.setDueDate(LocalDate.now().plusDays(7));
        request.setPriority(Priority.HIGH);
        request.setCourseId(courseId);
        return request;
    }

    @Test
    void createsAssignmentLinkedToCourse() {
        AssignmentResponse response = assignmentService.createAssignment(sampleRequest("Homework 1"));

        assertThat(response.getId()).isNotNull();
        assertThat(response.getCourseId()).isEqualTo(courseId);
        assertThat(response.getStatus()).isEqualTo(AssignmentStatus.TODO);
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    void throwsWhenCreatingAssignmentForMissingCourse() {
        AssignmentRequest request = sampleRequest("Homework 2");
        request.setCourseId(999_999L);

        assertThatThrownBy(() -> assignmentService.createAssignment(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void retrievesAssignmentById() {
        AssignmentResponse created = assignmentService.createAssignment(sampleRequest("Homework 3"));

        AssignmentResponse found = assignmentService.getAssignmentById(created.getId());

        assertThat(found.getTitle()).isEqualTo("Homework 3");
    }

    @Test
    void throwsWhenAssignmentDoesNotExist() {
        assertThatThrownBy(() -> assignmentService.getAssignmentById(999_999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updatesExistingAssignment() {
        AssignmentResponse created = assignmentService.createAssignment(sampleRequest("Homework 4"));

        AssignmentRequest update = sampleRequest("Homework 4 - revised");
        update.setPriority(Priority.LOW);

        AssignmentResponse updated = assignmentService.updateAssignment(created.getId(), update);

        assertThat(updated.getTitle()).isEqualTo("Homework 4 - revised");
        assertThat(updated.getPriority()).isEqualTo(Priority.LOW);
    }

    @Test
    void deletesExistingAssignment() {
        AssignmentResponse created = assignmentService.createAssignment(sampleRequest("Homework 5"));

        assignmentService.deleteAssignment(created.getId());

        assertThatThrownBy(() -> assignmentService.getAssignmentById(created.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void marksAssignmentAsCompleted() {
        AssignmentResponse created = assignmentService.createAssignment(sampleRequest("Homework 6"));

        AssignmentResponse completed = assignmentService.markAsCompleted(created.getId());

        assertThat(completed.getStatus()).isEqualTo(AssignmentStatus.COMPLETED);
    }

    @Test
    void filtersAssignmentsByStatus() {
        AssignmentResponse a = assignmentService.createAssignment(sampleRequest("Homework 7"));
        assignmentService.markAsCompleted(a.getId());
        assignmentService.createAssignment(sampleRequest("Homework 8"));

        List<AssignmentResponse> completed = assignmentService.getAssignmentsByStatus(AssignmentStatus.COMPLETED);

        assertThat(completed).extracting(AssignmentResponse::getId).contains(a.getId());
        assertThat(completed).allMatch(item -> item.getStatus() == AssignmentStatus.COMPLETED);
    }

    @Test
    void filtersAssignmentsByPriority() {
        AssignmentRequest lowPriority = sampleRequest("Homework 9");
        lowPriority.setPriority(Priority.LOW);
        AssignmentResponse low = assignmentService.createAssignment(lowPriority);

        List<AssignmentResponse> results = assignmentService.getAssignmentsByPriority(Priority.LOW);

        assertThat(results).extracting(AssignmentResponse::getId).contains(low.getId());
        assertThat(results).allMatch(item -> item.getPriority() == Priority.LOW);
    }

    @Test
    void filtersAssignmentsByCourse() {
        AssignmentResponse created = assignmentService.createAssignment(sampleRequest("Homework 10"));

        List<AssignmentResponse> results = assignmentService.getAssignmentsByCourse(courseId);

        assertThat(results).extracting(AssignmentResponse::getId).contains(created.getId());
    }

}
