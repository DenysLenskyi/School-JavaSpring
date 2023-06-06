package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "student_course", schema = "school")
public class StudentCourse {

    @EmbeddedId
    private StudentCoursePK id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    public StudentCourse() {
    }

    public StudentCourse(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentCourse that)) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (getStudent() != null ? !getStudent().equals(that.getStudent()) : that.getStudent() != null) return false;
        return getCourse() != null ? getCourse().equals(that.getCourse()) : that.getCourse() == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }
}