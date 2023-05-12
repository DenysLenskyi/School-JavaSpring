package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.StudentCourse;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/populate-test-tables.sql")
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
    }

    @Test
    void addStudentsCoursesTest() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper()
        );
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(students.get(0).getId());
        studentCourse.setCourseId(1);
        List<StudentCourse> studentsCourses = new ArrayList<>();
        studentsCourses.add(studentCourse);
        jdbcStudentCoursesDao.addStudentsCourses(studentsCourses);
        List<Map<String, Object>> test = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(test.size() == 1);
    }

    @Test
    void addStudentToCourseTest() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper()
        );
        jdbcStudentCoursesDao.addStudentToCourse(students.get(0).getId(),"Math");
        List<Map<String, Object>> test = jdbcTemplate.queryForList("select * from school.student_course");
        assertTrue(test.size() == 1);
    }

    @Test
    void isStudentEnrolledToCourseTest() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper()
        );
        jdbcStudentCoursesDao.addStudentToCourse(students.get(0).getId(),"Math");
        assertTrue(jdbcStudentCoursesDao.isStudentEnrolledToCourse(students.get(0).getId(),"Math"));
    }

    @Test
    void getStudentsFromCourse() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper()
        );
        jdbcStudentCoursesDao.addStudentToCourse(students.get(0).getId(),"Math");
        List<Student> enrolledStudents = jdbcStudentCoursesDao.getStudentsEnrolledToCourse("Math");
        assertTrue(enrolledStudents.size() == 1);
    }

    @Test
    void removeStudentFromCourseTest() {
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper()
        );
        jdbcStudentCoursesDao.addStudentToCourse(students.get(0).getId(),"Math");
        jdbcStudentCoursesDao.removeStudentFromCourse(students.get(0).getId(), "Math");
        List<Student> enrolledStudents = jdbcStudentCoursesDao.getStudentsEnrolledToCourse("Math");
        assertEquals(0, enrolledStudents.size());
    }
}