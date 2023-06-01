package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaGroupDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Group;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.StudentCourse;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.RandomDataCreator;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.SchoolJDBCCache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class PopulateTablesCommandTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JpaCourseDao jdbcCourseDao;
    @Autowired
    private JpaGroupDao jpaGroupDao;
    @Autowired
    private JpaStudentDao jpaStudentDao;
    @Autowired
    private JpaStudentCourseDao jpaStudentCourseDao;
    @Autowired
    private SchoolJDBCCache schoolJDBCCache;
    @PersistenceContext
    private EntityManager entityManager;
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
        List<Map<String, Object>> testCourses = jdbcTemplate.queryForList("select * from school.course");
        List<Map<String, Object>> testGroups = jdbcTemplate.queryForList("select * from school.group");
        List<Map<String, Object>> testStudents = jdbcTemplate.queryForList("select * from school.student");
        List<Map<String, Object>> testStudentCourse = jdbcTemplate.queryForList(
                "select * from school.student_course");
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
        List<Course> courses = List.of(new Course(2, "Sport", "Sport descr"));
        List<Group> groups = List.of(new Group(2, "BB-00"));
        List<Student> students = List.of(new Student(null, "Boris", "Jonsonuk"));
        StudentCourse studentCourse = new StudentCourse(
                entityManager.find(Student.class, schoolJDBCCache.getMinStudentId()),
                entityManager.find(Course.class, schoolJDBCCache.getMinCourseId()));
        Set<StudentCourse> studentCourses = Set.of(studentCourse);
        when(mockRandomDataCreator.getCoursesFromResources()).thenReturn(courses);
        when(mockRandomDataCreator.generateGroups(isA(Integer.class))).thenReturn(groups);
        when(mockRandomDataCreator.generateStudents(isA(Integer.class))).thenReturn(students);
        when(mockRandomDataCreator.enrollStudentsToCourses()).thenReturn(studentCourses);
    }
}