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
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcStudentDaoTest {

    private JdbcStudentDao jdbcStudentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcStudentDao = new JdbcStudentDao(jdbcTemplate);
    }

    @Test
    void deleteStudentTest() {
        jdbcStudentDao.addStudent(null, "Mark", "Mark");
        jdbcStudentDao.addStudent(null, "Mark", "Mark");
        jdbcStudentDao.addStudent(null, "Mark", "Mark");
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper()
        );
        jdbcStudentDao.deleteStudent(students.get(0).getId());
        List<Map<String, Object>> test = jdbcTemplate.queryForList("select * from school.student;");
        assertEquals(2, test.size());
    }

    @Test
    void addStudentsTest() {
        List<Student> students = new ArrayList<>();
        Student mark = new Student(1, null, "Mark", "Mark");
        students.add(mark);
        jdbcStudentDao.addStudents(students);
        List<Map<String, Object>> test = jdbcTemplate.queryForList("select * from school.student;");
        assertTrue(test.size() == 1);
    }

    @Test
    void addStudentTest() {
        jdbcStudentDao.addStudent(null, "Mark", "Mark");
        jdbcStudentDao.addStudent(null, "Mark", "Mark");
        jdbcStudentDao.addStudent(null, "Mark", "Mark");
        List<Map<String, Object>> test = jdbcTemplate.queryForList("select * from school.student;");
        assertTrue(test.size() == 3);
    }

    @Test
    void doesStudentExistTest() {
        jdbcStudentDao.addStudent(null, "Mark", "Mark");
        jdbcStudentDao.addStudent(null, "Mark", "Mark");
        jdbcStudentDao.addStudent(null, "Mark", "Mark");
        List<Student> students = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper()
        );
        assertTrue(jdbcStudentDao.doesStudentExist(students.get(0).getId()));
        assertFalse(jdbcStudentDao.doesStudentExist(5000));
    }
}