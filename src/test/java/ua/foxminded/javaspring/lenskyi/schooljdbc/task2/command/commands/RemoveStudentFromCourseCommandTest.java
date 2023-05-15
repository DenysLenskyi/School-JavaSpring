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
class RemoveStudentFromCourseCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcCourseDao jdbcCourseDao;
    private JdbcStudentDao jdbcStudentDao;
    private JdbcStudentCourseDao jdbcStudentCourseDao;
    private RemoveStudentFromCourseCommand removeStudentFromCourseCommand;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
        jdbcStudentDao = new JdbcStudentDao(jdbcTemplate);
        jdbcCourseDao = new JdbcCourseDao(jdbcTemplate);
        jdbcStudentCourseDao = new JdbcStudentCourseDao(jdbcTemplate);
        removeStudentFromCourseCommand = new RemoveStudentFromCourseCommand(jdbcStudentCourseDao,
                jdbcStudentDao, jdbcCourseDao);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void removeStudentFromCourseCommandCorrectTest() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        int studentId = students.get(0).getId();
        jdbcStudentCourseDao.executeQuery("insert into school.student_course (student_id, course_id) values" +
                "(" + studentId + ",1);");
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName("Math");
        removeStudentFromCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(studentCourse.size() == 0);
        assertEquals("Student removed from the course", outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandIncorrectCourseTest() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(students.get(0).getId());
        commandHolder.setCourseName("ChakraFlowTeaching");
        removeStudentFromCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(studentCourse.size() == 0);
        assertEquals("Wrong course name.\n" +
                "Available courses: Math, English, Biologic, Geography, Chemistry,\n" +
                "                   Physics, History, Finance, Sports, Etiquette.", outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandIncorrectStudentTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(666777);
        commandHolder.setCourseName("Math");
        removeStudentFromCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(studentCourse.size() == 0);
        assertEquals("Wrong student's id. This id doesn't exist in the database",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandAlreadyRemovedStudentTest() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        int studentId = students.get(0).getId();
        jdbcStudentCourseDao.executeQuery("insert into school.student_course (student_id, course_id) values" +
                "(" + studentId + ",1);");
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName("Math");
        removeStudentFromCourseCommand.execute(commandHolder);
        removeStudentFromCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(studentCourse.size() == 0);
        assertEquals("Student removed from the course\n" +
                "This student doesn't visit this course", outputStreamCaptor.toString().trim());
    }
}