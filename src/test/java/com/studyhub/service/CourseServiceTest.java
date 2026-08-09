package com.studyhub.service;

import com.studyhub.dto.CourseRequest;
import com.studyhub.dto.CourseResponse;
import com.studyhub.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    private CourseRequest sampleRequest(String code) {
        CourseRequest request = new CourseRequest();
        request.setName("Data Structures");
        request.setCode(code);
        request.setProfessor("Dr. Ionescu");
        request.setSemester("2026-1");
        return request;
    }

    @Test
    void createsCourseAndAssignsId() {
        CourseResponse response = courseService.createCourse(sampleRequest("CS201"));

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("Data Structures");
        assertThat(response.getCode()).isEqualTo("CS201");
    }

    @Test
    void retrievesAllCourses() {
        courseService.createCourse(sampleRequest("CS201"));
        courseService.createCourse(sampleRequest("CS202"));

        List<CourseResponse> courses = courseService.getAllCourses();

        assertThat(courses).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void retrievesCourseById() {
        CourseResponse created = courseService.createCourse(sampleRequest("CS203"));

        CourseResponse found = courseService.getCourseById(created.getId());

        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getCode()).isEqualTo("CS203");
    }

    @Test
    void throwsWhenCourseDoesNotExist() {
        assertThatThrownBy(() -> courseService.getCourseById(999_999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updatesExistingCourse() {
        CourseResponse created = courseService.createCourse(sampleRequest("CS204"));

        CourseRequest update = sampleRequest("CS204-B");
        update.setName("Advanced Data Structures");

        CourseResponse updated = courseService.updateCourse(created.getId(), update);

        assertThat(updated.getName()).isEqualTo("Advanced Data Structures");
        assertThat(updated.getCode()).isEqualTo("CS204-B");
    }

    @Test
    void deletesExistingCourse() {
        CourseResponse created = courseService.createCourse(sampleRequest("CS205"));

        courseService.deleteCourse(created.getId());

        assertThatThrownBy(() -> courseService.getCourseById(created.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}
