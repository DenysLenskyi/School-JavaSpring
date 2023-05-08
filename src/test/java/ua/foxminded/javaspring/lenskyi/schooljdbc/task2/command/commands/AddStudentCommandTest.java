package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/test_schema.sql"})
class AddStudentCommandTest {

    private AddStudentCommand addStudentCommand;
    private JdbcStudentDao jdbcStudentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcStudentDao = new JdbcStudentDao(jdbcTemplate);
        addStudentCommand = new AddStudentCommand(jdbcStudentDao);
        jdbcStudentDao.executeQuery("insert into school.group (name) values ('AA-00')");
    }

    @Test
    void addStudentTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(1);
        commandHolder.setStudentFirstName("Mark");
        commandHolder.setStudentLastName("Mark");
        addStudentCommand.execute(commandHolder);
        assertTrue(jdbcStudentDao.isStudentExists(1));
    }
}