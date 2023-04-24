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
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcGroupDaoTest {

    private static final String INIT_TABLES = """
            CREATE SCHEMA IF NOT EXISTS school;
            CREATE TABLE IF NOT EXISTS school.group (
                ID SERIAL PRIMARY KEY,
                NAME TEXT
            );
            CREATE TABLE IF NOT EXISTS school.student (
                ID SERIAL PRIMARY KEY,
                GROUP_ID INT,
                FIRST_NAME TEXT,
                LAST_NAME TEXT,
                CONSTRAINT GROUP_ID_FK
                FOREIGN KEY (GROUP_ID)
                REFERENCES school.group (ID)
            );
            """;

    private JdbcGroupDao jdbcGroupDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcGroupDao = new JdbcGroupDao(jdbcTemplate);
        jdbcGroupDao.jdbcTemplate.execute(INIT_TABLES);
    }

    @Test
    void addCoursesTest() {
        Group first = new Group(1, "first");
        Group second = new Group(2, "second");
        List<Group> groups = new ArrayList<>();
        groups.add(first);
        groups.add(second);
        jdbcGroupDao.addGroups(groups);
        List<Map<String, Object>> test = jdbcGroupDao.jdbcTemplate.queryForList("select * from school.group");
        assertEquals(2, test.size());
    }
}
