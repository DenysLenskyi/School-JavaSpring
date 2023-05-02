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
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCoursesDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EnrollStudentToCourseCommandTest {

    private static final String INIT_TABLES = """
            DROP SCHEMA IF EXISTS school CASCADE;
            CREATE SCHEMA school;
            CREATE TABLE IF NOT EXISTS school.course (
                ID SERIAL PRIMARY KEY,
                NAME TEXT,
                DESCRIPTION TEXT
            );
            CREATE TABLE IF NOT EXISTS school.student (
                ID SERIAL PRIMARY KEY,
                GROUP_ID INT,
                FIRST_NAME TEXT,
                LAST_NAME TEXT
            );
            CREATE TABLE IF NOT EXISTS school.student_course (
                STUDENT_ID INT,
                COURSE_ID INT,
                CONSTRAINT STUDENT_ID_FK
                FOREIGN KEY (STUDENT_ID)
                REFERENCES school.student (ID)
                ON DELETE CASCADE,
                CONSTRAINT COURSE_ID_FK
                FOREIGN KEY (COURSE_ID)
                REFERENCES school.course (ID)
                ON DELETE CASCADE
            );
            """;

    private EnrollStudentToCourseCommand enrollStudentToCourseCommand;
    private JdbcStudentCoursesDao jdbcStudentCoursesDao;
    private JdbcStudentDao jdbcStudentDao;
    private JdbcCourseDao jdbcCourseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcStudentCoursesDao = new JdbcStudentCoursesDao(jdbcTemplate);
        jdbcStudentDao = new JdbcStudentDao(jdbcTemplate);
        jdbcCourseDao = new JdbcCourseDao(jdbcTemplate);
        enrollStudentToCourseCommand = new EnrollStudentToCourseCommand(jdbcStudentCoursesDao,
                jdbcStudentDao, jdbcCourseDao);
        jdbcStudentCoursesDao.executeQuery(INIT_TABLES);
        jdbcStudentCoursesDao.executeQuery("insert into school.course (name, description) values ('Math','Math');");
        jdbcStudentCoursesDao.executeQuery("insert into school.student (group_id, first_name, last_name)" +
                " values(1, 'Mark','Mark');");
    }

    @Test
    void enrollStudentToCourseCommandTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(1);
        commandHolder.setCourseName("Math");
        enrollStudentToCourseCommand.execute(commandHolder);
        assertTrue(jdbcStudentCoursesDao.isStudentEnrolledToCourse(1, "Math"));
    }
}