package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.orm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class StudentDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void persistTest() {
        Student mark = new Student(100500, null, "Mark", "Markson");
        this.entityManager.persist(mark);
        Student bob = new Student(null, "Bob", "Markson");
        this.entityManager.persist(bob);
    }
}