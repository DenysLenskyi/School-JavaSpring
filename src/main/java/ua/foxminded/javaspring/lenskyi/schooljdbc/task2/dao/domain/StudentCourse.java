package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain;

import java.util.Set;

public class StudentCourse {

    private int studentId;
    private Set<Integer> courseId;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Set<Integer> getCourseId() {
        return courseId;
    }

    public void setCourseId(Set<Integer> courseId) {
        this.courseId = courseId;
    }
}

