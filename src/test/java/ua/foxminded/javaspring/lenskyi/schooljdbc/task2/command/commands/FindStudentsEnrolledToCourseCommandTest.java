package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = FindStudentsEnrolledToCourseCommandTest.class)
class FindStudentsEnrolledToCourseCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private FindStudentsEnrolledToCourseCommand findStudentsEnrolledToCourseCommand;
    @MockBean
    private JdbcCourseDao jdbcCourseDao;
    @MockBean
    private JdbcStudentCourseDao jdbcStudentCoursesDao;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        findStudentsEnrolledToCourseCommand = new FindStudentsEnrolledToCourseCommand(
                jdbcStudentCoursesDao, jdbcCourseDao);
        when(jdbcCourseDao.isCourseExists(isA(String.class))).thenReturn(true);
        Student mark = new Student(1, null, "Mark", "Markson");
        List<Student> test = new ArrayList<>();
        test.add(mark);
        when(jdbcStudentCoursesDao.getStudentsEnrolledToCourse(isA(String.class))).thenReturn(test);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
    @Test
    void findStudentsEnrolledToCourseCommandTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setCourseName("Math");
        findStudentsEnrolledToCourseCommand.execute(commandHolder);
        assertEquals("Student ID: 1 | Student name: Mark Markson", outputStreamCaptor.toString().trim());
    }
}