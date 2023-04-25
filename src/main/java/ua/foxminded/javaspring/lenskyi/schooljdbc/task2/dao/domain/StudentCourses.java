package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain;

import java.util.Set;

public class StudentCourses {

    private int studentId;
    private Set<Integer> courseIds;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Set<Integer> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(Set<Integer> courseIds) {
        this.courseIds = courseIds;
    }
}