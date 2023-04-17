package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.CourseRowMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = JdbcBaseDaoTest.DataSourceInitializer.class)
public class JdbcBaseDaoTest {

    @Container
    private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:15-alpine");

    private final RowMapper<Course> courseRowMapper = new CourseRowMapper();
    private JdbcBaseDao jdbcBaseDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcBaseDaoTest(JdbcBaseDao jdbcBaseDao, JdbcTemplate jdbcTemplate) {
        this.jdbcBaseDao = jdbcBaseDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public static class DataSourceInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + database.getJdbcUrl(),
                    "spring.datasource.username=" + database.getUsername(),
                    "spring.datasource.password=" + database.getPassword()
            );
        }
    }

    @Test
    @Transactional
    void simpleQueriesTest() {
        jdbcBaseDao.executeQuery("create schema if not exists test;");
        jdbcBaseDao.executeQuery("create table test.course (\n" +
                "id serial primary key,\n" +
                "name text,\n" +
                "description text\n" +
                ");");
        jdbcBaseDao.executeQuery("insert into test.course (name, description) values \n" +
                "('Gor', 'just a guy'),\n" +
                "('Denys', 'awesome dude');");
        Course gor = jdbcTemplate.queryForObject("select * from test.course where id = 1;", courseRowMapper);
        assertEquals(gor.getName(), "Gor");
    }
}
