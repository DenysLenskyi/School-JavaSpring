package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.transaction.Transactional;
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

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@Transactional
@Order(5)
class RemoveStudentFromCourseCommandTest {

    private final static String expectedIncorrectCourseName = "Wabalabubuduwoodoo";
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Autowired
    private JpaStudentCourseDao jpaStudentCourseDao;
    @Autowired
    private JpaStudentDao jpaStudentDao;
    @Autowired
    private JpaCourseDao jpaCourseDao;
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
        long studentId = jpaStudentDao.getMinStudentId() + 3;
        jpaStudentCourseDao.executeQuery("insert into school.student_course (student_id, course_id) values" +
                "(" + studentId + ",1);");
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName(jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId()).getName());
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        //asserts
        assertFalse(jpaStudentCourseDao.isStudentEnrolledToCourse(studentId,
                jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId()).getName()));
        assertEquals("Student removed from the course", outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandIncorrectCourseTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(jpaStudentDao.getMinStudentId());
        commandHolder.setCourseName(expectedIncorrectCourseName);
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        //asserts
        assertEquals("Wrong course name.\n" +
                "Available courses: Math, English, Biologic, Geography, Chemistry,\n" +
                "                   Physics, History, Finance, Sports, Etiquette.", outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandIncorrectStudentTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(jpaStudentDao.getMaxStudentId() + 1);
        commandHolder.setCourseName(jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId()).getName());
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        //asserts
        assertEquals("Wrong student's id. This id doesn't exist in the database",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandAlreadyRemovedStudentTest() {
        //arranges
        long studentId = jpaStudentDao.getMinStudentId() + 3;
        jpaStudentCourseDao.executeQuery("insert into school.student_course (student_id, course_id) values" +
                "(" + studentId + ",1);");
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName(jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId()).getName());
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        removeStudentFromCourseCommand.execute(commandHolder);
        //asserts
        assertTrue(outputStreamCaptor.toString().trim().contains("Student removed from the course\n" +
                "This student doesn't visit this course"));
    }
}