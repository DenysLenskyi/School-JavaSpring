package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class SchoolJDBCCacheTest {

    private static final String INSERT_INTO_COURSE =
            "insert into school.course (name, description) values('english', 'english');";

    @Autowired
    private SchoolJDBCCache schoolJDBCCache;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getMinCourseIdTest() {
        int minCourseId = schoolJDBCCache.getMinCourseId();
        assertEquals(1, minCourseId);
    }

    @Test
    void getMaxCourseIdTest() {
        int maxCourseId = schoolJDBCCache.getMaxCourseId();
        assertEquals(1, maxCourseId);
    }
}