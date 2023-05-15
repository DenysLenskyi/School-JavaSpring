package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class AddStudentCommandTest {

    private AddStudentCommand addStudentCommand;
    private JdbcStudentDao jdbcStudentDao;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
        jdbcStudentDao = new JdbcStudentDao(jdbcTemplate);
        addStudentCommand = new AddStudentCommand(jdbcStudentDao);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void addStudentCorrectTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(1L);
        commandHolder.setStudentFirstName("Mark10");
        commandHolder.setStudentLastName("Markson");
        addStudentCommand.execute(commandHolder);
        Student student = jdbcTemplate.queryForObject("select * from school.student where first_name = 'Mark10';",
                new StudentRowMapper());
        assertEquals("Student added", outputStreamCaptor.toString().trim());
        assertEquals(1, student.getGroupId());
        assertEquals("Mark10", student.getFirstName());
        assertEquals("Markson", student.getLastName());
        jdbcStudentDao.executeQuery("delete from school.student where first_name='Mark10'");
    }

    @Test
    void addStudentIncorrectGroupId11Test() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(11L);
        commandHolder.setStudentFirstName("Mark11");
        commandHolder.setStudentLastName("Markson");
        addStudentCommand.execute(commandHolder);
        assertEquals("Group id could be in range from 0 to 10\n" +
                "Student not added, check your input", outputStreamCaptor.toString().trim());
            try {
                jdbcTemplate.queryForObject("select * from school.student where first_name = 'Mark11';",
                        new StudentRowMapper());
            } catch (EmptyResultDataAccessException e) {
                assertTrue(e.getMessage().contains("Incorrect result size: expected 1, actual 0"));
            }
    }

    @Test
    void addStudentNegativeGroupIdTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(-11L);
        commandHolder.setStudentFirstName("Mark12");
        commandHolder.setStudentLastName("Markson");
        addStudentCommand.execute(commandHolder);
        assertEquals("Group id could be in range from 0 to 10\n" +
                "Student not added, check your input", outputStreamCaptor.toString().trim());
        try {
            jdbcTemplate.queryForObject("select * from school.student where first_name = 'Mark12';",
                    new StudentRowMapper());
        } catch (EmptyResultDataAccessException e) {
            assertTrue(e.getMessage().contains("Incorrect result size: expected 1, actual 0"));
        }
    }
}