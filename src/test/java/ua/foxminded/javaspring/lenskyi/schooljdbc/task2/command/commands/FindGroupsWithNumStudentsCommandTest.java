package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolderBuilder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcGroupDao;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class FindGroupsWithNumStudentsCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private FindGroupsWithNumStudentsCommand findGroupsWithNumStudentsCommand;
    private JdbcGroupDao jdbcGroupDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        jdbcGroupDao = new JdbcGroupDao(jdbcTemplate);
        findGroupsWithNumStudentsCommand = new FindGroupsWithNumStudentsCommand(jdbcGroupDao);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void findGroupsWithNumStudentsTest() {
        CommandHolder commandHolder = new CommandHolderBuilder();
        commandHolder.setNumStudents(1);
        jdbcGroupDao.executeQuery("insert into school.student (group_id, first_name, last_name) values" +
                "(1, 'Mark', 'Markson');");
        findGroupsWithNumStudentsCommand.execute(commandHolder);
        assertEquals("""
                Groups with less or equal than 1 students
                Group ID:  1 | Group name:  AA-00""", outputStreamCaptor.toString().trim());
        jdbcGroupDao.executeQuery("delete from school.student where group_id = 1");
    }
}