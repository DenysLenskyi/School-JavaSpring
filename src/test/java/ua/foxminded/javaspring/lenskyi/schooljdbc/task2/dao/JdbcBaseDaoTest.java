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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcBaseDaoTest {

    private static final String INIT_TABLE = """
            CREATE SCHEMA IF NOT EXISTS school;
            CREATE TABLE IF NOT EXISTS school.course (
                ID SERIAL PRIMARY KEY,
                NAME TEXT,
                DESCRIPTION TEXT
            );
            """;
    private static final String PLAIN_INSERT =
            "insert into school.course (name, description) values ('Math', 'Math')";
    private JdbcBaseDao jdbcBaseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcBaseDao = new JdbcBaseDao(jdbcTemplate);
    }

    @Test
    void createTablesTest() {
        jdbcBaseDao.executeQuery(INIT_TABLE);
        jdbcBaseDao.executeQuery(PLAIN_INSERT);
        List<Map<String, Object>> courses = jdbcBaseDao.jdbcTemplate.queryForList("select * from school.course");
        assertEquals(1, courses.size());
    }
}