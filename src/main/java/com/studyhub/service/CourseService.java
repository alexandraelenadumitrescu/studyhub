package com.studyhub.service;

import com.studyhub.dto.CourseRequest;
import com.studyhub.dto.CourseResponse;
import com.studyhub.entity.Course;
import com.studyhub.exception.ResourceNotFoundException;
import com.studyhub.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseResponse createCourse(CourseRequest request) {
        Course course = new Course();
        course.setName(request.getName());
        course.setCode(request.getCode());
        course.setProfessor(request.getProfessor());
        course.setSemester(request.getSemester());
        Course saved = courseRepository.save(course);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        return toResponse(findCourseOrThrow(id));
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = findCourseOrThrow(id);
        course.setName(request.getName());
        course.setCode(request.getCode());
        course.setProfessor(request.getProfessor());
        course.setSemester(request.getSemester());
        return toResponse(course);
    }

    public void deleteCourse(Long id) {
        Course course = findCourseOrThrow(id);
        courseRepository.delete(course);
    }

    private Course findCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    private CourseResponse toResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .code(course.getCode())
                .professor(course.getProfessor())
                .semester(course.getSemester())
                .build();
    }

}
