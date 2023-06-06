package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.*;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Group;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.StudentCourse;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.RandomDataCreator;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class PopulateTablesCommandTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JpaCourseDao jdbcCourseDao;
    @Autowired
    private JpaGroupDao jpaGroupDao;
    @Autowired
    private JpaStudentDao jpaStudentDao;
    @Autowired
    private JpaStudentCourseDao jpaStudentCourseDao;
    @Autowired
    private SchoolCache schoolCache;
    private PopulateTablesCommand populateTablesCommand;
    private RandomDataCreator mockRandomDataCreator = Mockito.mock(RandomDataCreator.class);

    @BeforeEach
    void setup() {
        populateTablesCommand = new PopulateTablesCommand(jdbcCourseDao, jpaGroupDao, jpaStudentDao,
                jpaStudentCourseDao, mockRandomDataCreator);
    }

    @Test
    void populateTablesCommandTest() {
        //arranges
        setupRandomDataCreator();
        populateTablesCommand.execute(new CommandHolder());
        //act
        List<Course> testCourses = entityManager.createQuery("select c from Course c", Course.class)
                .getResultList();
        List<Group> testGroups = entityManager.createQuery("select g from Group g", Group.class)
                .getResultList();
        ;
        List<Student> testStudents = entityManager.createQuery("select s from Student s", Student.class)
                .getResultList();
        List<StudentCourse> testStudentCourse = entityManager.createQuery("select sc from StudentCourse sc",
                StudentCourse.class).getResultList();
        //asserts
        assertEquals(2, testCourses.size());
        assertEquals(2, testGroups.size());
        assertEquals(6, testStudents.size());
        assertEquals(1, testStudentCourse.size());
        jdbcCourseDao.executeQuery("delete from school.course where name = 'Sport';");
        jpaGroupDao.executeQuery("delete from school.group where name = 'BB-00';");
        jpaStudentDao.executeQuery("delete from school.student where first_name = 'Boris';");
        jpaStudentCourseDao.executeQuery("delete from school.student_course where course_id = 2;");
    }

    private void setupRandomDataCreator() {
        List<Course> courses = List.of(new Course("Sport", "Sport descr"));
        List<Group> groups = List.of(new Group("BB-00"));
        List<Student> students = List.of(new Student(null, "Boris", "Jonsonuk"));
        StudentCourse studentCourse = new StudentCourse(
                entityManager.find(Student.class, schoolCache.getMinStudentId()),
                entityManager.find(Course.class, schoolCache.getMinCourseId()));
        Set<StudentCourse> studentCourses = Set.of(studentCourse);
        when(mockRandomDataCreator.getCoursesFromResources()).thenReturn(courses);
        when(mockRandomDataCreator.generateGroups(isA(Integer.class))).thenReturn(groups);
        when(mockRandomDataCreator.generateStudents(isA(Integer.class))).thenReturn(students);
        when(mockRandomDataCreator.enrollStudentsToCourses()).thenReturn(studentCourses);
    }
}