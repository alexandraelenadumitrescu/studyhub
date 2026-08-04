package com.studyhub.repository;

import com.studyhub.entity.Assignment;
import com.studyhub.entity.AssignmentStatus;
import com.studyhub.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByStatus(AssignmentStatus status);

    List<Assignment> findByPriority(Priority priority);

    List<Assignment> findByCourseId(Long courseId);

}
