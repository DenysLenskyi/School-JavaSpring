package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/test_schema.sql"})
class RemoveStudentFromCourseCommandTest {

    private RemoveStudentFromCourseCommand removeStudentFromCourseCommand;
    private JdbcStudentCourseDao jdbcStudentCoursesDao;
    private JdbcStudentDao jdbcStudentDao;
    private JdbcCourseDao jdbcCourseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcStudentCoursesDao = new JdbcStudentCourseDao(jdbcTemplate);
        jdbcStudentDao = new JdbcStudentDao(jdbcTemplate);
        jdbcCourseDao = new JdbcCourseDao(jdbcTemplate);
        removeStudentFromCourseCommand = new RemoveStudentFromCourseCommand(jdbcStudentCoursesDao,
                jdbcStudentDao, jdbcCourseDao);
        jdbcStudentCoursesDao.executeQuery("insert into school.course (name, description) values ('Math','Math');");
        jdbcStudentCoursesDao.executeQuery("insert into school.group (name) values ('AA-00')");
        jdbcStudentCoursesDao.executeQuery("insert into school.student (group_id, first_name, last_name)" +
                " values(1, 'Mark','Mark');");
    }

    @Test
    void removeStudentFromCourseCommandTest() {
        jdbcStudentCoursesDao.executeQuery("insert into school.student_course (student_id, course_id) values (1,1)");
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(1);
        commandHolder.setCourseName("Math");
        removeStudentFromCourseCommand.execute(commandHolder);
        assertFalse(jdbcStudentCoursesDao.isStudentEnrolledToCourse(1, "Math"));
    }
}