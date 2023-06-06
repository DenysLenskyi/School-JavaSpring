package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "student", schema = "school")
public class Student {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @ManyToOne
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private Group group;
    @OneToMany(mappedBy = "student")
    Set<StudentCourse> studentCourse;

    public Student() {
    }

    public Student(Long groupId, String firstName, String lastName) {
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}