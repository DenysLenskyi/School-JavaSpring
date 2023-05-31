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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class FindStudentsEnrolledToCourseCommandTest {

    private static final String expectedCourseName = "Math";
    private static final String expectedIncorrectCourseName = "Numerology";
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Autowired
    private FindStudentsEnrolledToCourseCommand findStudentsEnrolledToCourseCommand;
    @Autowired
    private JdbcStudentCourseDao jdbcStudentCourseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void findStudentsEnrolledToCourseCommandCorrectTest() {
        //arranges
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        int studentId = students.get(0).getId();
        jdbcStudentCourseDao.executeQuery("insert into school.student_course (student_id, course_id) values" +
                "(" + studentId + ",1);");
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setCourseName(expectedCourseName);
        //act
        findStudentsEnrolledToCourseCommand.execute(commandHolder);
        //asserts
        assertEquals("Student ID: " + studentId +
                " | Student name: Mark Markson", outputStreamCaptor.toString().trim());
        jdbcStudentCourseDao.executeQuery("delete from school.student_course where student_id = " + studentId);
    }

    @Test
    void findStudentsEnrolledToCourseCommandIncorrectCourseTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setCourseName(expectedIncorrectCourseName);
        //act
        findStudentsEnrolledToCourseCommand.execute(commandHolder);
        //asserts
        assertEquals("Failed...\n" +
                "Available courses: Math, English, Biologic, Geography, Chemistry,\n" +
                "                   Physics, History, Finance, Sports, Etiquette.", outputStreamCaptor.toString().trim());
    }
}