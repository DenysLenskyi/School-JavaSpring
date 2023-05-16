package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcGroupDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Group;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.StudentCourse;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.RandomDataCreator;

import java.util.List;
import java.util.Map;

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
    private JdbcCourseDao jdbcCourseDao;
    @Autowired
    private JdbcGroupDao jdbcGroupDao;
    @Autowired
    private JdbcStudentDao jdbcStudentDao;
    @Autowired
    private JdbcStudentCourseDao jdbcStudentCourseDao;
    private PopulateTablesCommand populateTablesCommand;
    private RandomDataCreator mockRandomDataCreator = Mockito.mock(RandomDataCreator.class);

    @BeforeEach
    void setup() {
        populateTablesCommand = new PopulateTablesCommand(jdbcCourseDao, jdbcGroupDao, jdbcStudentDao,
                jdbcStudentCourseDao, mockRandomDataCreator);
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
        jdbcGroupDao.executeQuery("delete from school.group where name = 'BB-00';");
        jdbcStudentDao.executeQuery("delete from school.student where first_name = 'Boris';");
        jdbcStudentCourseDao.executeQuery("delete from school.student_course where course_id = 2;");
    }

    private void setupRandomDataCreator() {
        List<Course> courses = List.of(new Course(2, "Sport", "Sport descr"));
        List<Group> groups = List.of(new Group(2, "BB-00"));
        List<Student> students = List.of(new Student(1, null, "Boris", "Jonsonuk"));
        StudentCourse studentCourse = new StudentCourse();
        List<Student> studentsWithRightId = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        studentCourse.setStudentId(studentsWithRightId.get(0).getId());
        studentCourse.setCourseId(2);
        List<StudentCourse> studentCourses = List.of(studentCourse);
        when(mockRandomDataCreator.getCoursesFromResources()).thenReturn(courses);
        when(mockRandomDataCreator.generateGroups(isA(Integer.class))).thenReturn(groups);
        when(mockRandomDataCreator.generateStudents(isA(Integer.class))).thenReturn(students);
        when(mockRandomDataCreator.generateStudentCourseRelation(isA(Integer.class))).thenReturn(studentCourses);
    }
}