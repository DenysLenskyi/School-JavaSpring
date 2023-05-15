package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class EnrollStudentToCourseCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcCourseDao jdbcCourseDao;
    private JdbcStudentDao jdbcStudentDao;
    private JdbcStudentCourseDao jdbcStudentCourseDao;
    private EnrollStudentToCourseCommand enrollStudentToCourseCommand;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
        jdbcStudentDao = new JdbcStudentDao(jdbcTemplate);
        jdbcCourseDao = new JdbcCourseDao(jdbcTemplate);
        jdbcStudentCourseDao = new JdbcStudentCourseDao(jdbcTemplate);
        enrollStudentToCourseCommand = new EnrollStudentToCourseCommand(jdbcStudentCourseDao,
                jdbcStudentDao,jdbcCourseDao);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void enrollStudentToCourseCommandCorrectTest() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(students.get(0).getId());
        commandHolder.setCourseName("Math");
        enrollStudentToCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(studentCourse.size() == 1);
        jdbcStudentCourseDao.executeQuery("delete from school.student_course where course_id = 1");
    }

    @Test
    void enrollStudentToCourseCommandIncorrectStudentTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(777666123);
        commandHolder.setCourseName("Math");
        enrollStudentToCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(studentCourse.size() == 0);
        assertEquals("Wrong student's id. This id doesn't exist in the database", outputStreamCaptor.toString().trim());
    }

    @Test
    void enrollStudentToCourseCommandIncorrectCourseTest() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(students.get(0).getId());
        commandHolder.setCourseName("Numerology");
        enrollStudentToCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(studentCourse.size() == 0);
        assertEquals("Wrong course name.\n" +
                "Available courses: Math, English, Biologic, Geography, Chemistry,\n" +
                "                   Physics, History, Finance, Sports, Etiquette.", outputStreamCaptor.toString().trim());
    }

    @Test
    void enrollStudentToCourseCommandAlreadyEnrolledStudentTest() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(students.get(0).getId());
        commandHolder.setCourseName("Math");
        enrollStudentToCourseCommand.execute(commandHolder);
        enrollStudentToCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(studentCourse.size() == 1);
        assertEquals("Student enrolled to the course\n" +
                "This student already visits this course", outputStreamCaptor.toString().trim());
        jdbcStudentCourseDao.executeQuery("delete from school.student_course where course_id = 1");
    }
}