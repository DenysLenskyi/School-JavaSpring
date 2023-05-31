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
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;
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

    private final static String expectedCourseName = "Math";
    private final static String expectedIncorrectCourseName = "Wabalabubuduwoodoo";
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcStudentCourseDao jdbcStudentCourseDao;
    @Autowired
    private RemoveStudentFromCourseCommand removeStudentFromCourseCommand;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void removeStudentFromCourseCommandCorrectTest() {
        //arranges
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        int studentId = students.get(0).getId();
        jdbcStudentCourseDao.executeQuery("insert into school.student_course (student_id, course_id) values" +
                "(" + studentId + ",1);");
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName(expectedCourseName);
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        //asserts
        assertTrue(studentCourse.size() == 0);
        assertEquals("Student removed from the course", outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandIncorrectCourseTest() {
        //arranges
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(students.get(0).getId());
        commandHolder.setCourseName(expectedIncorrectCourseName);
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        //asserts
        assertTrue(studentCourse.size() == 0);
        assertEquals("Wrong course name.\n" +
                "Available courses: Math, English, Biologic, Geography, Chemistry,\n" +
                "                   Physics, History, Finance, Sports, Etiquette.", outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandIncorrectStudentTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(666777);
        commandHolder.setCourseName(expectedCourseName);
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        //asserts
        assertTrue(studentCourse.size() == 0);
        assertEquals("Wrong student's id. This id doesn't exist in the database",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandAlreadyRemovedStudentTest() {
        //arranges
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        int studentId = students.get(0).getId();
        jdbcStudentCourseDao.executeQuery("insert into school.student_course (student_id, course_id) values" +
                "(" + studentId + ",1);");
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName(expectedCourseName);
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        removeStudentFromCourseCommand.execute(commandHolder);
        List<Map<String, Object>> studentCourse = jdbcTemplate.queryForList("select * from school.student_course");
        //asserts
        assertTrue(studentCourse.size() == 0);
        assertEquals("Student removed from the course\n" +
                "This student doesn't visit this course", outputStreamCaptor.toString().trim());
    }
}