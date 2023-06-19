package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.CourseRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.GroupRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.StudentRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Group;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.RandomDataCreator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@Order(1)
class PopulateTablesCommandTest {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StudentRepository studentRepository;
    private PopulateTablesCommand populateTablesCommand;
    private RandomDataCreator mockRandomDataCreator = Mockito.mock(RandomDataCreator.class);

    @BeforeEach
    void setup() {
        populateTablesCommand = new PopulateTablesCommand(courseRepository, groupRepository,
                studentRepository, mockRandomDataCreator);
    }

    @Test
    @Transactional
    void populateTablesCommandTest() {
        //arranges
        setupRandomDataCreator();
        populateTablesCommand.execute(new CommandHolder());
        //act
        List<Course> testCourses = courseRepository.findAll();
        List<Group> testGroups = groupRepository.findAll();
        ;
        List<Student> testStudents = studentRepository.findAll();
        //asserts
        assertEquals(2, testCourses.size());
        assertEquals(2, testGroups.size());
        assertEquals(6, testStudents.size());
    }

    private void setupRandomDataCreator() {
        List<Course> courses = List.of(new Course("Sport", "Sport descr"));
        List<Group> groups = List.of(new Group("BB-00"));
        List<Student> students = List.of(new Student(null, "Boris", "Jonsonuk"));
        when(mockRandomDataCreator.getCoursesFromResources()).thenReturn(courses);
        when(mockRandomDataCreator.generateGroups(isA(Integer.class))).thenReturn(groups);
        when(mockRandomDataCreator.generateStudents(isA(Integer.class))).thenReturn(students);
    }
}