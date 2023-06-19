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
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.CourseRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.StudentRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.RandomDataCreator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RandomDataCreator randomDataCreator;

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
        Student student = randomDataCreator.generateStudents(1).get(0);
        studentRepository.save(student);
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setCourseName(courseRepository.findById(courseRepository.getMinCourseId()).get().getName());
        //act
        findStudentsEnrolledToCourseCommand.execute(commandHolder);
        //asserts
        assertTrue(outputStreamCaptor.toString().trim().contains("Student ID: " + student.getId() +
                " | Student name: " + student.getFirstName() + " " + student.getLastName()));
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