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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@Order(2)
class FindStudentsEnrolledToCourseCommandTest {

    private static final String expectedIncorrectCourseName = "Numerology";
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Autowired
    private FindStudentsEnrolledToCourseCommand findStudentsEnrolledToCourseCommand;
    @Autowired
    private JpaStudentCourseDao jpaStudentCourseDao;
    @Autowired
    private JpaStudentDao jpaStudentDao;
    @Autowired
    private JpaCourseDao jpaCourseDao;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    @Transactional
    void findStudentsEnrolledToCourseCommandCorrectTest() {
        //arranges
        long studentId = jpaStudentDao.getMinStudentId() + 1;
        jpaStudentCourseDao.executeQuery("insert into school.student_course (student_id, course_id) values" +
                "(" + studentId + ",1);");
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setCourseName(jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId()).getName());
        //act
        findStudentsEnrolledToCourseCommand.execute(commandHolder);
        //asserts
        assertEquals("Student ID: " + studentId +
                " | Student name: Mark Markson", outputStreamCaptor.toString().trim());
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