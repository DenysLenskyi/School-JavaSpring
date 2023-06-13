package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolderBuilder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaGroupDao;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class FindGroupsWithNumStudentsCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Autowired
    private FindGroupsWithNumStudentsCommand findGroupsWithNumStudentsCommand;
    @Autowired
    private JpaGroupDao jpaGroupDao;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    @Transactional
    void findGroupsWithNumStudentsTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolderBuilder();
        commandHolder.setNumStudents(1);
        jpaGroupDao.executeQuery("insert into school.student (group_id, first_name, last_name) values" +
                "(1, 'Mark', 'Markson');");
        //act
        findGroupsWithNumStudentsCommand.execute(commandHolder);
        //asserts
        assertEquals("""
                Groups with less or equal than 1 students
                Group ID:  1 | Group name:  AA-00""", outputStreamCaptor.toString().trim());
        //jpaGroupDao.executeQuery("delete from school.student where group_id = 1");
    }
}