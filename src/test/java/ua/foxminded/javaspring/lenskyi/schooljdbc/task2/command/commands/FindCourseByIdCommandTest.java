package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = FindCourseByIdCommand.class)
class FindCourseByIdCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private FindCourseByIdCommand findCourseByIdCommand;
    @MockBean
    private JdbcCourseDao jdbcCourseDao;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        findCourseByIdCommand = new FindCourseByIdCommand(jdbcCourseDao);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void findCourseByIdTest() {
        Course course = new Course(1, "Math", "Math");
        when(jdbcCourseDao.findCourseById(1)).thenReturn(course);
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setCourseId(1);
        findCourseByIdCommand.execute(commandHolder);
        assertEquals("Course ID:  1 | Course name:  Math | Description:  Math",
                outputStreamCaptor.toString().trim());
    }
}