package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "course", schema = "school")
public class Group {

    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "NAME")
    private String name;

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}