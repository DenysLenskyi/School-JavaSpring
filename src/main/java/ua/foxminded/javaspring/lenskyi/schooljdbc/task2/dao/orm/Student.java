package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm;

import jakarta.persistence.*;

@Entity
@Table(name = "student", schema = "school")
public class Student {
    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;

    public Student() {
    }

    public Student(Long groupId, String firstName, String lastName) {
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(int id, Long groupId, String firstName, String lastName) {
        this.id = id;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getGroupId() {
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