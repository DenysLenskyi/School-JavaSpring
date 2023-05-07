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
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/test_schema.sql"})
class JdbcCourseDaoTest {

    private JdbcCourseDao jdbcCourseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcCourseDao = new JdbcCourseDao(jdbcTemplate);
    }

    @Test
    void addCoursesTest() {
        Course math = new Course(1, "Math", "smth about numbers");
        Course english = new Course(2, "Eng", "smth about letters");
        List<Course> courses = new ArrayList<>();
        courses.add(math);
        courses.add(english);
        jdbcCourseDao.addCourses(courses);
        List<Map<String, Object>> test = jdbcCourseDao.jdbcTemplate.queryForList("select * from school.course");
        assertEquals(2, test.size());
    }

    @Test
    void findCourseByIdTest() {
        jdbcCourseDao.executeQuery("insert into school.course (id, name, description) " +
                "values (1, 'Math', 'smth about numbers');");
        Course test = jdbcCourseDao.findCourseById(1);
        assertEquals(1, test.getId());
        assertEquals("Math", test.getName());
        assertEquals("smth about numbers", test.getDescription());
    }

    @Test
    void isCourseExistsTest() {
        jdbcCourseDao.executeQuery("insert into school.course (id, name, description) " +
                "values (1, 'Math', 'smth about numbers');");
        assertTrue(jdbcCourseDao.isCourseExists("Math"));
    }
}