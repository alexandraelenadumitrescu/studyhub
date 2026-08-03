package com.studyhub.dto;

public class CourseResponse {

    private final Long id;
    private final String name;
    private final String code;
    private final String professor;
    private final String semester;

    private CourseResponse(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.code = builder.code;
        this.professor = builder.professor;
        this.semester = builder.semester;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getProfessor() {
        return professor;
    }

    public String getSemester() {
        return semester;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String code;
        private String professor;
        private String semester;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder professor(String professor) {
            this.professor = professor;
            return this;
        }

        public Builder semester(String semester) {
            this.semester = semester;
            return this;
        }

        public CourseResponse build() {
            return new CourseResponse(this);
        }
    }

}
