package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain;

import java.util.Set;

public class StudentCourses {

    private int studentId;
    private Set<Integer> coursesId;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Set<Integer> getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(Set<Integer> coursesId) {
        this.coursesId = coursesId;
    }
}

