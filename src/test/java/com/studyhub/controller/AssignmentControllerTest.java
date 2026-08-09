package com.studyhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyhub.dto.AssignmentRequest;
import com.studyhub.dto.CourseRequest;
import com.studyhub.entity.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long courseId;

    @BeforeEach
    void setUp() throws Exception {
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName("Databases");
        courseRequest.setCode("CS501-" + System.nanoTime());
        courseRequest.setProfessor("Dr. Stanescu");
        courseRequest.setSemester("2026-1");

        String responseBody = mockMvc.perform(post("/api/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(courseRequest)))
                .andReturn().getResponse().getContentAsString();

        this.courseId = objectMapper.readTree(responseBody).get("id").asLong();
    }

    private AssignmentRequest sampleRequest(String title) {
        AssignmentRequest request = new AssignmentRequest();
        request.setTitle(title);
        request.setDescription("Design the ER diagram and submit the schema");
        request.setDueDate(LocalDate.now().plusDays(5));
        request.setPriority(Priority.MEDIUM);
        request.setCourseId(courseId);
        return request;
    }

    @Test
    void createAssignmentReturns201() throws Exception {
        mockMvc.perform(post("/api/assignments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sampleRequest("Project Milestone 1"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.courseId").value(courseId));
    }

    @Test
    void createAssignmentWithBlankTitleReturns400() throws Exception {
        AssignmentRequest invalid = sampleRequest("");

        mockMvc.perform(post("/api/assignments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAssignmentWithoutDueDateReturns400() throws Exception {
        AssignmentRequest invalid = sampleRequest("Project Milestone 2");
        invalid.setDueDate(null);

        mockMvc.perform(post("/api/assignments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAssignmentForMissingCourseReturns404() throws Exception {
        AssignmentRequest request = sampleRequest("Project Milestone 3");
        request.setCourseId(999_999L);

        mockMvc.perform(post("/api/assignments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMissingAssignmentReturns404() throws Exception {
        mockMvc.perform(get("/api/assignments/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void completeAssignmentUpdatesStatus() throws Exception {
        String responseBody = mockMvc.perform(post("/api/assignments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sampleRequest("Project Milestone 4"))))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(responseBody).get("id").asLong();

        mockMvc.perform(patch("/api/assignments/{id}/complete", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void filtersAssignmentsByStatusQueryParam() throws Exception {
        mockMvc.perform(get("/api/assignments").param("status", "TODO"))
                .andExpect(status().isOk());
    }

    @Test
    void filtersAssignmentsByPriorityQueryParam() throws Exception {
        mockMvc.perform(get("/api/assignments").param("priority", "MEDIUM"))
                .andExpect(status().isOk());
    }

}
