package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest(classes = EnrollStudentToCourseCommand.class)
class EnrollStudentToCourseCommandTest {

    private EnrollStudentToCourseCommand enrollStudentToCourseCommand;
    @MockBean
    private JdbcStudentCourseDao jdbcStudentCoursesDao;
    @MockBean
    private JdbcStudentDao jdbcStudentDao;
    @MockBean
    private JdbcCourseDao jdbcCourseDao;

    @BeforeEach
    void setUp() {
        enrollStudentToCourseCommand = new EnrollStudentToCourseCommand(jdbcStudentCoursesDao,
                jdbcStudentDao, jdbcCourseDao);
        when(jdbcCourseDao.isCourseExists(isA(String.class))).thenReturn(true);
        when(jdbcStudentDao.doesStudentExist(isA(Integer.class))).thenReturn(true);
        when(jdbcStudentCoursesDao.isStudentEnrolledToCourse(isA(Integer.class), isA(String.class)))
                .thenReturn(false);
    }

    @Test
    void enrollStudentToCourseCommandTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(1);
        commandHolder.setCourseName("Math");
        enrollStudentToCourseCommand.execute(commandHolder);
        verify(jdbcStudentCoursesDao, times(1)).addStudentToCourse(1, "Math");
    }
}