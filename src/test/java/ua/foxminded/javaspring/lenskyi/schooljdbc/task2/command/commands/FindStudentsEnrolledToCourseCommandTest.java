package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FindStudentsEnrolledToCourseCommandTest {

    private FindStudentsEnrolledToCourseCommand findStudentsEnrolledToCourseCommand;
    private JdbcCourseDao jdbcCourseDao;
    private JdbcStudentCourseDao jdbcStudentCoursesDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcStudentCoursesDao = new JdbcStudentCourseDao(jdbcTemplate);
        jdbcCourseDao = new JdbcCourseDao(jdbcTemplate);
        findStudentsEnrolledToCourseCommand = new FindStudentsEnrolledToCourseCommand(jdbcStudentCoursesDao, jdbcCourseDao);
        jdbcStudentCoursesDao.executeQuery("insert into school.course (name, description) values ('Math','Math');");
        jdbcStudentCoursesDao.executeQuery("insert into school.group (name) values ('AA-00')");
        jdbcStudentCoursesDao.executeQuery("insert into school.student (group_id, first_name, last_name)" +
                " values(1, 'Mark','Mark');");
        jdbcStudentCoursesDao.executeQuery("insert into school.student_course (student_id, course_id)" +
                " values(1, 1);");
    }

    @Test
    void findStudentsEnrolledToCourseCommandTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setCourseName("Math");
        findStudentsEnrolledToCourseCommand.execute(commandHolder);
        assertEquals(1, findStudentsEnrolledToCourseCommand.getListStudents().size());
    }
}