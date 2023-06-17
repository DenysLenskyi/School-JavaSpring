package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

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
class FindCourseByIdCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Autowired
    private FindCourseByIdCommand findCourseByIdCommand;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
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

    @Test
    void findCourseByIdDoesntExistTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setCourseId(100);
        findCourseByIdCommand.execute(commandHolder);
        assertEquals("Minimal course id -  1\n" +
                        "Maximal course id -  1",
                outputStreamCaptor.toString().trim());
    }
}