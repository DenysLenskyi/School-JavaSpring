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
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class FindCourseByIdCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private FindCourseByIdCommand findCourseByIdCommand;
    private JdbcCourseDao jdbcCourseDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        jdbcCourseDao = new JdbcCourseDao(jdbcTemplate);
        findCourseByIdCommand = new FindCourseByIdCommand(jdbcCourseDao);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void findCourseByIdTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setCourseId(1);
        findCourseByIdCommand.execute(commandHolder);
        assertEquals("Course ID:  1 | Course name:  Math | Description:  Math",
                outputStreamCaptor.toString().trim());
    }
}