package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain;

import java.util.Objects;

public class StudentCourse {

    private int studentId;
    private int courseId;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentCourse that = (StudentCourse) o;
        return (studentId == that.getStudentId()) &&
                (courseId == that.getCourseId());
    }
}