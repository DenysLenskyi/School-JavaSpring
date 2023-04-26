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
public class JdbcStudentCourseDaoTest {

    private static final String INIT_TABLES = """
            DROP SCHEMA IF EXISTS school CASCADE;
            CREATE SCHEMA school;
            CREATE TABLE IF NOT EXISTS school.course (
                ID SERIAL PRIMARY KEY,
                NAME TEXT,
                DESCRIPTION TEXT
            );
            CREATE TABLE IF NOT EXISTS school.student (
                ID SERIAL PRIMARY KEY,
                GROUP_ID INT,
                FIRST_NAME TEXT,
                LAST_NAME TEXT
            );
            CREATE TABLE IF NOT EXISTS school.student_course (
                STUDENT_ID INT,
                COURSE_ID INT,
                CONSTRAINT STUDENT_ID_FK
                FOREIGN KEY (STUDENT_ID)
                REFERENCES school.student (ID)
                ON DELETE CASCADE,
                CONSTRAINT COURSE_ID_FK
                FOREIGN KEY (COURSE_ID)
                REFERENCES school.course (ID)
                ON DELETE CASCADE
            );
            """;

    private JdbcStudentCoursesDao jdbcStudentCoursesDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcStudentCoursesDao = new JdbcStudentCoursesDao(jdbcTemplate);
        jdbcTemplate.execute(INIT_TABLES);
        jdbcStudentCoursesDao.executeQuery("insert into school.course (name, description) values ('Math','Math');");
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