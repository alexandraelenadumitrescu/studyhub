package com.studyhub.dto;

import jakarta.validation.constraints.NotBlank;

public class CourseRequest {

    @NotBlank(message = "Course name must not be blank")
    private String name;

    @NotBlank(message = "Course code must not be blank")
    private String code;

    private String professor;

    private String semester;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

}
