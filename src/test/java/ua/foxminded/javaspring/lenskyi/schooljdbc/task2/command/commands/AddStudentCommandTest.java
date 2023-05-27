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

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class AddStudentCommandTest {

    private final static Long EXPECTED_CORRECT_GROUP_ID = 1L;
    private final static Long EXPECTED_OUT_OF_BOUNDS_GROUP_ID = 11L;
    private final static Long EXPECTED_NEGATIVE_GROUP_ID = -11L;
    private final static String EXPECTED_FIRST_NAME_10 = "Mark10";
    private final static String EXPECTED_FIRST_NAME_11 = "Mark11";
    private final static String EXPECTED_FIRST_NAME_12 = "Mark12";
    private final static String EXPECTED_SECOND_NAME = "Markson";
    private final static String EXPECTED_SYSTEM_OUT_IF_STUDENT_ADDED = "Student added";
    private final static String EXPECTED_SYSTEM_OUT_IF_NOT_ADDED = "Incorrect group_id, check info\n" +
            "Student not added, check your input";
    private final static String INCORRECT_RESULT_SIZE_EXPECTED_1_ACTUAL_0 =
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
        commandHolder.setGroupId(EXPECTED_CORRECT_GROUP_ID);
        commandHolder.setStudentFirstName(EXPECTED_FIRST_NAME_10);
        commandHolder.setStudentLastName(EXPECTED_SECOND_NAME);
        //act
        addStudentCommand.execute(commandHolder);
        Student actualStudent = jdbcTemplate.queryForObject("select * from school.student where first_name = 'Mark10';",
                new StudentRowMapper());
        //asserts
        assertEquals(EXPECTED_SYSTEM_OUT_IF_STUDENT_ADDED, outputStreamCaptor.toString().trim());
        assertEquals(EXPECTED_CORRECT_GROUP_ID, actualStudent.getGroupId());
        assertEquals(EXPECTED_FIRST_NAME_10, actualStudent.getFirstName());
        assertEquals(EXPECTED_SECOND_NAME, actualStudent.getLastName());
        jdbcStudentDao.executeQuery("delete from school.student where first_name='Mark10'");
    }

    @Test
    void addStudentIncorrectGroupId11Test() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(EXPECTED_OUT_OF_BOUNDS_GROUP_ID);
        commandHolder.setStudentFirstName(EXPECTED_FIRST_NAME_11);
        commandHolder.setStudentLastName(EXPECTED_SECOND_NAME);
        //act
        addStudentCommand.execute(commandHolder);
        //asserts
        assertEquals(EXPECTED_SYSTEM_OUT_IF_NOT_ADDED, outputStreamCaptor.toString().trim());
        Exception e = assertThrows(EmptyResultDataAccessException.class,
                () -> jdbcTemplate.queryForObject("select * from school.student where first_name = 'Mark11';",
                        new StudentRowMapper()));
        assertEquals(INCORRECT_RESULT_SIZE_EXPECTED_1_ACTUAL_0, e.getMessage());
    }

    @Test
    void addStudentNegativeGroupIdTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(EXPECTED_NEGATIVE_GROUP_ID);
        commandHolder.setStudentFirstName(EXPECTED_FIRST_NAME_12);
        commandHolder.setStudentLastName(EXPECTED_SECOND_NAME);
        //act
        addStudentCommand.execute(commandHolder);
        //asserts
        assertEquals(EXPECTED_SYSTEM_OUT_IF_NOT_ADDED, outputStreamCaptor.toString().trim());
        Exception e = assertThrows(EmptyResultDataAccessException.class,
                () -> jdbcTemplate.queryForObject("select * from school.student where first_name = 'Mark12';",
                        new StudentRowMapper()));
        assertEquals(INCORRECT_RESULT_SIZE_EXPECTED_1_ACTUAL_0, e.getMessage());
    }

    @Test
    void addStudentDataViolationTest() {
        //arranges
        CommandHolder commandHolder = CommandHolderBuilder.buildCommandFromInputString(
                "add_student --group_id=CommandHolderSetGroupIdDataViolationTest --first_name=t --last_name=t");
        //asserts
        assertNull(commandHolder.getGroupId());
        Exception e = assertThrows(NullPointerException.class,
                () -> addStudentCommand.execute(commandHolder));
    }
}