package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.SchoolCache;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.StudentCourse;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

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
    private SchoolCache schoolCache;
    @PersistenceContext
    private EntityManager entityManager;

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
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(schoolCache.getMinStudentId());
        commandHolder.setCourseName(expectedCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        List<StudentCourse> studentCourse = entityManager.createQuery("select sc from StudentCourse sc",
                StudentCourse.class).getResultList();
        //asserts
        assertTrue(studentCourse.size() == 1);
        jpaStudentCourseDao.executeQuery("delete from school.student_course where course_id = 1");
    }

    @Test
    void enrollStudentToCourseCommandIncorrectStudentTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(777666123);
        commandHolder.setCourseName(expectedCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        List<StudentCourse> studentCourse = entityManager.createQuery("select sc from StudentCourse sc",
                StudentCourse.class).getResultList();
        //asserts
        assertTrue(studentCourse.size() == 0);
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
        List<StudentCourse> studentCourse = entityManager.createQuery("select sc from StudentCourse sc",
                StudentCourse.class).getResultList();
        //asserts
        assertTrue(studentCourse.size() == 0);
        assertEquals("Wrong course name.\n" +
                "Available courses: Math, English, Biologic, Geography, Chemistry,\n" +
                "                   Physics, History, Finance, Sports, Etiquette.", outputStreamCaptor.toString().trim());
    }

    @Test
    void enrollStudentToCourseCommandAlreadyEnrolledStudentTest() {
        //asserts
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(schoolCache.getMinStudentId());
        commandHolder.setCourseName(expectedCourseName);
        //act
        enrollStudentToCourseCommand.execute(commandHolder);
        enrollStudentToCourseCommand.execute(commandHolder);
        List<StudentCourse> studentCourse = entityManager.createQuery("select sc from StudentCourse sc",
                StudentCourse.class).getResultList();
        //asserts
        assertTrue(studentCourse.size() == 1);
        assertTrue(outputStreamCaptor.toString().trim().contains("This student already visits this course"));
        jpaStudentCourseDao.executeQuery("delete from school.student_course where course_id = 1");
    }
}