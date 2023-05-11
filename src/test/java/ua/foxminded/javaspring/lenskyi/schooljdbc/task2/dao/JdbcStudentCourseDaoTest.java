package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.StudentCourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcStudentCourseDaoTest {

    private JdbcStudentCourseDao jdbcStudentCoursesDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcStudentCoursesDao = new JdbcStudentCourseDao(jdbcTemplate);
        jdbcStudentCoursesDao.executeQuery("insert into school.course (name, description) values ('Math','Math');");
        jdbcStudentCoursesDao.executeQuery("insert into school.group (name) values ('AA-00')");
        jdbcStudentCoursesDao.executeQuery("insert into school.student (group_id, first_name, last_name)" +
                " values(1, 'Mark','Mark');");
    }

    @Test
    void addStudentsCoursesTest() {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(1);
        studentCourse.setCourseId(1);
        List<StudentCourse> studentsCourses = new ArrayList<>();
        studentsCourses.add(studentCourse);
        jdbcStudentCoursesDao.addStudentsCourses(studentsCourses);
        List<Map<String, Object>> test = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(test.size() == 1);
    }

    @Test
    void addStudentToCourseTest() {
        jdbcStudentCoursesDao.addStudentToCourse(1,"Math");
        List<Map<String, Object>> test = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(test.size() == 1);
    }

    @Test
    void isStudentEnrolledToCourseTest() {
        jdbcStudentCoursesDao.addStudentToCourse(1,"Math");
        assertTrue(jdbcStudentCoursesDao.isStudentEnrolledToCourse(1,"Math"));
    }

    @Test
    void getStudentsFromCourse() {
        jdbcStudentCoursesDao.addStudentToCourse(1,"Math");
        List<Student> students = jdbcStudentCoursesDao.getStudentsEnrolledToCourse("Math");
        assertTrue(students.size() == 1);
    }

    @Test
    void removeStudentFromCourseTest() {
        jdbcStudentCoursesDao.addStudentToCourse(1,"Math");
        jdbcStudentCoursesDao.removeStudentFromCourse(1, "Math");
        List<Student> students = jdbcStudentCoursesDao.getStudentsEnrolledToCourse("Math");
        assertTrue(students.size() == 0);
    }
}