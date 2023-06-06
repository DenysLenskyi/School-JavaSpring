package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

@Entity
@Table(name = "course", schema = "school")
public class Course {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course")
    private Collection<StudentCourse> studentCourse = new LinkedHashSet<StudentCourse>();

    public Course() {
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        if (getId() != course.getId()) return false;
        if (getName() != null ? !getName().equals(course.getName()) : course.getName() != null) return false;
        if (getDescription() != null ? !getDescription().equals(course.getDescription()) : course.getDescription() != null)
            return false;
        return Objects.equals(studentCourse, course.studentCourse);
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (studentCourse != null ? studentCourse.hashCode() : 0);
        return result;
    }
}