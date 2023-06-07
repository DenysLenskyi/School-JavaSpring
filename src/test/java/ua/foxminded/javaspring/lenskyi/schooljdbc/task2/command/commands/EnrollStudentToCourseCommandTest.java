package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.SchoolCache;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
    private JpaStudentCourseDao jpaStudentCourseDao;
    @Autowired
    private EnrollStudentToCourseCommand enrollStudentToCourseCommand;
    @Autowired
    private JpaStudentDao jpaStudentDao;
    @Autowired
    private SchoolCache schoolCache;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
        schoolCache.setMinStudentId(jpaStudentDao.getMinStudentId());
        schoolCache.setMaxStudentId(jpaStudentDao.getMaxStudentId());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void enrollStudentToCourseCommandCorrectTest() {
        //arranges
        long studentId = schoolCache.getMinStudentId();
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName(expectedCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        //asserts
        assertTrue(jpaStudentCourseDao.isStudentEnrolledToCourse(schoolCache.getMinStudentId(), expectedCourseName));
        jpaStudentCourseDao.executeQuery("delete from school.student_course where student_id = " + studentId);
    }

    @Test
    void enrollStudentToCourseCommandIncorrectStudentTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(schoolCache.getMaxStudentId() + 1);
        commandHolder.setCourseName(expectedCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        //asserts
        assertEquals("Wrong student's id. This id doesn't exist in the database", outputStreamCaptor.toString().trim());
    }

    @Test
    void enrollStudentToCourseCommandIncorrectCourseTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(schoolCache.getMinStudentId());
        commandHolder.setCourseName(expectedIncorrectCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        //asserts
        assertEquals("Wrong course name.\n" +
                "Available courses: Math, English, Biologic, Geography, Chemistry,\n" +
                "                   Physics, History, Finance, Sports, Etiquette.", outputStreamCaptor.toString().trim());
    }

    @Test
    void enrollStudentToCourseCommandAlreadyEnrolledStudentTest() {
        //asserts
        long studentId = schoolCache.getMinStudentId();
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName(expectedCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        enrollStudentToCourseCommand.execute(commandHolder);
        //asserts
        assertTrue(jpaStudentCourseDao.isStudentEnrolledToCourse(studentId, expectedCourseName));
        assertTrue(outputStreamCaptor.toString().trim().contains("This student already visits this course"));
        jpaStudentCourseDao.executeQuery("delete from school.student_course where student_id = " + studentId);
    }
}