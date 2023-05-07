package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
        jdbcStudentDao.executeQuery("insert into school.group (name) values ('AA-00')");
    }

    @Test
    @Sql({"/test_schema.sql"})
    void addStudentsTest() {
        List<Student> students = new ArrayList<>();
        Student mark = new Student(1, 1, "Mark", "Mark");
        students.add(mark);
        jdbcStudentDao.addStudents(students);
        List<Map<String, Object>> test = jdbcTemplate.queryForList("select * from school.student;");
        assertTrue(test.size() == 1);
    }

    @Test
    @Sql({"/test_schema.sql"})
    void addStudentTest() {
        jdbcStudentDao.addStudent(1, "Mark", "Mark");
        jdbcStudentDao.addStudent(1, "Mark", "Mark");
        jdbcStudentDao.addStudent(1, "Mark", "Mark");
        List<Map<String, Object>> test = jdbcTemplate.queryForList("select * from school.student;");
        assertTrue(test.size() == 3);
    }

    @Test
    @Sql({"/test_schema.sql"})
    void deleteStudentTest() {
        jdbcStudentDao.addStudent(1, "Mark", "Mark");
        jdbcStudentDao.addStudent(1, "Mark", "Mark");
        jdbcStudentDao.addStudent(1, "Mark", "Mark");
        jdbcStudentDao.deleteStudent(3);
        List<Map<String, Object>> test = jdbcTemplate.queryForList("select * from school.student;");
        assertTrue(test.size() == 2);
    }

    @Test
    @Sql({"/test_schema.sql"})
    void isStudentExistsTest() {
        jdbcStudentDao.addStudent(1, "Mark", "Mark");
        jdbcStudentDao.addStudent(1, "Mark", "Mark");
        jdbcStudentDao.addStudent(1, "Mark", "Mark");
        assertTrue(jdbcStudentDao.isStudentExists(1));
    }
}