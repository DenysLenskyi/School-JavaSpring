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
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RemoveStudentFromCourseCommand removeStudentFromCourseCommand;
    @Autowired
    private RandomDataCreator randomDataCreator;

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
        Student student = randomDataCreator.generateStudents(1).get(0);
        studentRepository.save(student);
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(student.getId());
        commandHolder.setCourseName(courseRepository.findById(courseRepository.getMinCourseId()).get().getName());
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        //asserts
        assertFalse(studentRepository.doesStudentVisitTheCourse(student.getId(),
                courseRepository.findById(courseRepository.getMinCourseId()).get().getId()));
        assertEquals("Student removed from the course", outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandIncorrectCourseTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentRepository.getMinStudentId());
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
        commandHolder.setStudentId(studentRepository.getMaxStudentId() + 1);
        commandHolder.setCourseName(courseRepository.findById(courseRepository.getMinCourseId()).get().getName());
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        //asserts
        assertEquals("Wrong student's id. This id doesn't exist in the database",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void removeStudentFromCourseCommandAlreadyRemovedStudentTest() {
        //arranges
        Student student = randomDataCreator.generateStudents(1).get(0);
        studentRepository.save(student);
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(student.getId());
        commandHolder.setCourseName(courseRepository.findById(courseRepository.getMinCourseId()).get().getName());
        //act
        removeStudentFromCourseCommand.execute(commandHolder);
        removeStudentFromCourseCommand.execute(commandHolder);
        //asserts
        assertFalse(studentRepository.doesStudentVisitTheCourse(student.getId(),
                courseRepository.findById(courseRepository.getMinCourseId()).get().getId()));
        assertTrue(outputStreamCaptor.toString().trim().contains("Student removed from the course\n" +
                "This student doesn't visit this course"));
    }
}