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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@Transactional
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

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Autowired
    private AddStudentCommand addStudentCommand;

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
        //asserts
        assertEquals(EXPECTED_SYSTEM_OUT_IF_STUDENT_ADDED, outputStreamCaptor.toString().trim());
    }

    @Test
    void addStudentCorrectZeroGroupIdTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(0L);
        commandHolder.setStudentFirstName(EXPECTED_FIRST_NAME_10);
        commandHolder.setStudentLastName(EXPECTED_SECOND_NAME);
        addStudentCommand.execute(commandHolder);
        assertEquals(EXPECTED_SYSTEM_OUT_IF_STUDENT_ADDED, outputStreamCaptor.toString().trim());
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
    }
}