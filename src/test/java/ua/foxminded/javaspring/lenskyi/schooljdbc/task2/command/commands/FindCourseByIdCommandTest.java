package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FindCourseByIdCommandTest {

    private static final String SETUP_TABLE_FOR_TEST = """
            CREATE SCHEMA IF NOT EXISTS school;
            CREATE TABLE IF NOT EXISTS school.course (
                ID SERIAL PRIMARY KEY,
                NAME TEXT,
                DESCRIPTION TEXT
            );
            INSERT INTO school.course (name, description) VALUES ('Math', 'Math');
            """;

    //@InjectMocks
    private FindCourseByIdCommand findCourseByIdCommand;
    //@Mock
    private JdbcCourseDao jdbcCourseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcCourseDao = new JdbcCourseDao(jdbcTemplate);
        findCourseByIdCommand = new FindCourseByIdCommand(jdbcCourseDao);
        jdbcCourseDao.executeQuery(SETUP_TABLE_FOR_TEST);
        //MockitoAnnotations.openMocks(this);
    }

    @Test
    void findCourseByIdTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setCourseId(1);
        findCourseByIdCommand.execute(commandHolder);
        assertEquals("Math", findCourseByIdCommand.getCourse().getName());

        //jdbcCourseDao.executeQuery("INSERT INTO school.course (name, description) VALUES ('Math', 'Math');");
        //assertTrue(jdbcCourseDao.isCourseExists("Math"));
    }
}