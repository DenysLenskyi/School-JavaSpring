package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
class InfoCommandTest {

    private static final String INFO_LINE_1 = """
            info
               prints available commands and how to use them
            exit
               exits the app
            find_course --course_id={value}
               prints course's info by course id which could be in range """;
    private static final String INFO_LINE_2 = """
            find_groups --num_students={value}
               prints groups with less or equal student's number (max 30 students in one group)
            find_students_course --course_name={value}
               example: --course_name=History - prints students enrolled to course
            add_student --group_id={value} --first_name={value} --last_name={value}
               adds new student; group id should 0 or in range """;
    private static final String INFO_LINE_3 = """
            delete_student --student_id={value}
               deletes student by student id which could be in range """;
    private static final String INFO_LINE_4 = """
            add_student_course --student_id={value} --course_name={value}
               adds student to course
            delete_student_course --student_id={value} --course_name={value}
               deletes student from course""";
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Autowired
    private InfoCommand infoCommand;

    @Test
    void infoCommandTest() {
        infoCommand.execute(new CommandHolder());
        assertTrue(outputStreamCaptor.toString().trim().contains(INFO_LINE_1));
        assertTrue(outputStreamCaptor.toString().trim().contains(INFO_LINE_2));
        assertTrue(outputStreamCaptor.toString().trim().contains(INFO_LINE_3));
        assertTrue(outputStreamCaptor.toString().trim().contains(INFO_LINE_4));
    }
}