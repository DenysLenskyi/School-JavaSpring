package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolderBuilder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class AddStudentCommandTest {

    private final static Long expectedCorrectGroupId = 1L;
    private final static Long expectedOutOfBoundsGroupId = 11L;
    private final static Long expectedNegativeGroupId = -11L;
    private final static String expectedFirstName10 = "Mark10";
    private final static String expectedFirstName11 = "Mark11";
    private final static String expectedFirstName12 = "Mark12";
    private final static String expectedSecondName = "Markson";
    private final static String expectedSystemOutIfStudentAdded = "Student added";
    private final static String expectedSystemOutIfNotAdded = "Group id could be in range from 0 to 10\n" +
            "Student not added, check your input";
    private final static String expectedExceptionMessageForEmptyResultDataAccess =
            "Incorrect result size: expected 1, actual 0";

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Autowired
    private AddStudentCommand addStudentCommand;
    @Autowired
    private JdbcStudentDao jdbcStudentDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void addStudentCorrectTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(expectedCorrectGroupId);
        commandHolder.setStudentFirstName(expectedFirstName10);
        commandHolder.setStudentLastName(expectedSecondName);
        //act
        addStudentCommand.execute(commandHolder);
        Student actualStudent = jdbcTemplate.queryForObject("select * from school.student where first_name = 'Mark10';",
                new StudentRowMapper());
        //asserts
        assertEquals(expectedSystemOutIfStudentAdded, outputStreamCaptor.toString().trim());
        assertEquals(expectedCorrectGroupId, actualStudent.getGroupId());
        assertEquals(expectedFirstName10, actualStudent.getFirstName());
        assertEquals(expectedSecondName, actualStudent.getLastName());
        jdbcStudentDao.executeQuery("delete from school.student where first_name='Mark10'");
    }

    @Test
    void addStudentIncorrectGroupId11Test() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(expectedOutOfBoundsGroupId);
        commandHolder.setStudentFirstName(expectedFirstName11);
        commandHolder.setStudentLastName(expectedSecondName);
        //act
        addStudentCommand.execute(commandHolder);
        //asserts
        assertEquals(expectedSystemOutIfNotAdded, outputStreamCaptor.toString().trim());
        Exception e = assertThrows(EmptyResultDataAccessException.class,
                () -> jdbcTemplate.queryForObject("select * from school.student where first_name = 'Mark11';",
                        new StudentRowMapper()));
        assertEquals(expectedExceptionMessageForEmptyResultDataAccess, e.getMessage());
    }

    @Test
    void addStudentNegativeGroupIdTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(expectedNegativeGroupId);
        commandHolder.setStudentFirstName(expectedFirstName12);
        commandHolder.setStudentLastName(expectedSecondName);
        //act
        addStudentCommand.execute(commandHolder);
        //asserts
        assertEquals(expectedSystemOutIfNotAdded, outputStreamCaptor.toString().trim());
        Exception e = assertThrows(EmptyResultDataAccessException.class,
                () -> jdbcTemplate.queryForObject("select * from school.student where first_name = 'Mark12';",
                        new StudentRowMapper()));
        assertEquals(expectedExceptionMessageForEmptyResultDataAccess, e.getMessage());
    }

    @Test
    void addStudentDataViolationTest() {
        //arranges
        AtomicReference<CommandHolder> commandHolder = new AtomicReference<>(new CommandHolder());
        //asserts
        Exception e = assertThrows(NumberFormatException.class,
                () -> commandHolder.set(CommandHolderBuilder.buildCommandFromInputString(
                        "add_student --group_id=test --first_name=dataViolationTest --last_name=test")));
    }
}