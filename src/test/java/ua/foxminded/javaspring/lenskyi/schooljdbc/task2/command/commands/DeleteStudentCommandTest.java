package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.SchoolCache;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class DeleteStudentCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Autowired
    private DeleteStudentCommand deleteStudentCommand;
    @Autowired
    private JpaStudentDao jpaStudentDao;
    @Autowired
    private SchoolCache schoolCache;
    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void deleteStudentCorrectTest() {
        //arranges
        List<Student> studentsBeforeDelete = entityManager.createQuery("select s from Student s",
                Student.class).getResultList();
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentsBeforeDelete.get(0).getId());
        //act
        deleteStudentCommand.execute(commandHolder);
        List<Student> studentsAfterDelete = entityManager.createQuery("select s from Student s",
                Student.class).getResultList();
        //asserts
        assertEquals("Student deleted", outputStreamCaptor.toString().trim());
        assertEquals(studentsBeforeDelete.size() - 1, studentsAfterDelete.size());
        jpaStudentDao.executeQuery("INSERT INTO school.student (group_id, first_name, last_name) VALUES\n" +
                "    (null, 'Mark', 'Markson');");
    }

    @Test
    void deleteStudentNoSuchStudentIdTest() {
        //arranges
        List<Student> studentsBeforeDelete = entityManager.createQuery("select s from Student s",
                Student.class).getResultList();
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(schoolCache.getMaxStudentId() + 1);
        //act
        deleteStudentCommand.execute(commandHolder);
        List<Student> studentsAfterDelete = entityManager.createQuery("select s from Student s",
                Student.class).getResultList();
        //asserts
        assertEquals(studentsBeforeDelete.size(), studentsAfterDelete.size());
        assertEquals("Can't find this student id...", outputStreamCaptor.toString().trim());
    }
}