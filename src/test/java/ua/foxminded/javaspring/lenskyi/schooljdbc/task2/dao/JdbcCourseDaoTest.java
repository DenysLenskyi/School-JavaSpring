package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcCourseDaoTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresqlTestContainer.getInstance();

    private final RowMapper<Course> courseRowMapper = new CourseRowMapper();
    private JdbcBaseDao jdbcBaseDao;
    private JdbcTemplate jdbcTemplate;
    

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
