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
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.CourseRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.StudentCourseRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.StudentRepository;

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
    private StudentCourseRepository studentCourseRepository;
    @Autowired
    private EnrollStudentToCourseCommand enrollStudentToCourseCommand;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

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
        long studentId = studentRepository.getMinStudentId();
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName(courseRepository.findById(courseRepository.getMinCourseId()).get().getName());
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        //asserts
        assertTrue(studentCourseRepository.isStudentEnrolledToCourse(studentRepository.getMinStudentId(),
                courseRepository.findById(courseRepository.getMinCourseId()).get().getName()));
    }

    @Test
    void enrollStudentToCourseCommandIncorrectStudentTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentRepository.getMaxStudentId() + 1);
        commandHolder.setCourseName(courseRepository.findById(courseRepository.getMinCourseId()).get().getName());
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        //asserts
        assertEquals("Wrong student's id. This id doesn't exist in the database", outputStreamCaptor.toString().trim());
    }

    @Test
    void enrollStudentToCourseCommandIncorrectCourseTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentRepository.getMinStudentId());
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
        long studentId = studentRepository.getMinStudentId();
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        commandHolder.setCourseName(courseRepository.findById(courseRepository.getMinCourseId()).get().getName());
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        enrollStudentToCourseCommand.execute(commandHolder);
        //asserts
        assertTrue(studentCourseRepository.isStudentEnrolledToCourse(studentId,
                courseRepository.findById(courseRepository.getMinCourseId()).get().getName()));
        assertTrue(outputStreamCaptor.toString().trim().contains("This student already visits this course"));
    }
}