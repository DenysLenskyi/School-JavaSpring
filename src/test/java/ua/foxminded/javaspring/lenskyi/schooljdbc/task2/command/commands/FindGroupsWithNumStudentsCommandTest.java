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