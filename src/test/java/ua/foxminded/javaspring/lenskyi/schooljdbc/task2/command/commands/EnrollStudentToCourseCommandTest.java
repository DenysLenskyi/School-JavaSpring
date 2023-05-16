package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;
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

    private final static String expectedCourseName = "Math";
    private final static String expectedIncorrectCourseName = "Numerology";
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcStudentCourseDao jdbcStudentCourseDao;
    @Autowired
    private EnrollStudentToCourseCommand enrollStudentToCourseCommand;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void enrollStudentToCourseCommandCorrectTest() {
        //arranges
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(students.get(0).getId());
        commandHolder.setCourseName(expectedCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        //asserts
        assertTrue(studentCourse.size() == 1);
        jdbcStudentCourseDao.executeQuery("delete from school.student_course where course_id = 1");
    }

    @Test
    void enrollStudentToCourseCommandIncorrectStudentTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(777666123);
        commandHolder.setCourseName(expectedCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        //asserts
        assertTrue(studentCourse.size() == 0);
        assertEquals("Wrong student's id. This id doesn't exist in the database", outputStreamCaptor.toString().trim());
    }

    @Test
    void enrollStudentToCourseCommandIncorrectCourseTest() {
        //arranges
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(students.get(0).getId());
        commandHolder.setCourseName(expectedIncorrectCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        //asserts
        assertTrue(studentCourse.size() == 0);
        assertEquals("Wrong course name.\n" +
                "Available courses: Math, English, Biologic, Geography, Chemistry,\n" +
                "                   Physics, History, Finance, Sports, Etiquette.", outputStreamCaptor.toString().trim());
    }

    @Test
    void enrollStudentToCourseCommandAlreadyEnrolledStudentTest() {
        //asserts
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(students.get(0).getId());
        commandHolder.setCourseName(expectedCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        enrollStudentToCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        //asserts
        assertTrue(studentCourse.size() == 1);
        assertEquals("Student enrolled to the course\n" +
                "This student already visits this course", outputStreamCaptor.toString().trim());
        jdbcStudentCourseDao.executeQuery("delete from school.student_course where course_id = 1");
    }
}