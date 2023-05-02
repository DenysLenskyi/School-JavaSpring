package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolderBuilder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcGroupDao;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FindGroupsWithNumStudentsCommandTest {

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

    private static final String TEST_INSERT = """
            insert into school.group (id, name) values
            (1, 'AA-00'),
            (2, 'BB-00');
            insert into school.student (id, group_id, first_name, last_name) values
            (1, 1, 'Mark','Markson'),
            (2, 2, 'Mark2','Mark2son'),
            (3, 2, 'Mark2','Mark2son');
            """;

    private FindGroupsWithNumStudentsCommand findGroupsWithNumStudentsCommand;

    private JdbcGroupDao jdbcGroupDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        jdbcGroupDao = new JdbcGroupDao(jdbcTemplate);
        findGroupsWithNumStudentsCommand = new FindGroupsWithNumStudentsCommand(jdbcGroupDao);
        jdbcGroupDao.executeQuery(INIT_TABLES);
        jdbcGroupDao.executeQuery(TEST_INSERT);
    }

    @Test
    void findGroupsWithNumStudentsTest() {
        CommandHolder commandHolder = new CommandHolderBuilder();
        commandHolder.setNumStudents(1);
        findGroupsWithNumStudentsCommand.execute(commandHolder);
        assertEquals(1, findGroupsWithNumStudentsCommand.getListGroups().size());
    }
}