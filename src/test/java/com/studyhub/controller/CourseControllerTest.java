package com.studyhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyhub.dto.CourseRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseRequest sampleRequest(String code) {
        CourseRequest request = new CourseRequest();
        request.setName("Operating Systems");
        request.setCode(code);
        request.setProfessor("Dr. Marin");
        request.setSemester("2026-1");
        return request;
    }

    @Test
    void createCourseReturns201() throws Exception {
        mockMvc.perform(post("/api/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sampleRequest("CS401"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.code").value("CS401"));
    }

    @Test
    void createCourseWithBlankNameReturns400() throws Exception {
        CourseRequest invalid = sampleRequest("CS402");
        invalid.setName("");

        mockMvc.perform(post("/api/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCourseWithBlankCodeReturns400() throws Exception {
        CourseRequest invalid = sampleRequest("");

        mockMvc.perform(post("/api/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMissingCourseReturns404() throws Exception {
        mockMvc.perform(get("/api/courses/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCourseByIdReturnsCreatedCourse() throws Exception {
        String responseBody = mockMvc.perform(post("/api/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sampleRequest("CS403"))))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(responseBody).get("id").asLong();

        mockMvc.perform(get("/api/courses/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("CS403"));
    }

    @Test
    void deleteMissingCourseReturns404() throws Exception {
        mockMvc.perform(delete("/api/courses/{id}", 999_999L))
                .andExpect(status().isNotFound());
    }

}
