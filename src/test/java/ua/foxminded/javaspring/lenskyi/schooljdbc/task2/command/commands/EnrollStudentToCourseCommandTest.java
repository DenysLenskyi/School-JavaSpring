package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentDao;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@Order(4)
class EnrollStudentToCourseCommandTest {

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
    private JpaCourseDao jpaCourseDao;

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
        long studentId = jpaStudentDao.getMinStudentId().get();
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName(jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId().get()).getName());
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        //asserts
        assertTrue(jpaStudentCourseDao.isStudentEnrolledToCourse(jpaStudentDao.getMinStudentId().get(),
                jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId().get()).getName()));
    }

    @Test
    void enrollStudentToCourseCommandIncorrectStudentTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(jpaStudentDao.getMaxStudentId().get() + 1);
        commandHolder.setCourseName(jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId().get()).getName());
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        //asserts
        assertEquals("Wrong student's id. This id doesn't exist in the database", outputStreamCaptor.toString().trim());
    }

    @Test
    void enrollStudentToCourseCommandIncorrectCourseTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(jpaStudentDao.getMinStudentId().get());
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
        long studentId = jpaStudentDao.getMinStudentId().get();
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName(jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId().get()).getName());
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        enrollStudentToCourseCommand.execute(commandHolder);
        //asserts
        assertTrue(jpaStudentCourseDao.isStudentEnrolledToCourse(studentId,
                jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId().get()).getName()));
        assertTrue(outputStreamCaptor.toString().trim().contains("This student already visits this course"));
    }
}