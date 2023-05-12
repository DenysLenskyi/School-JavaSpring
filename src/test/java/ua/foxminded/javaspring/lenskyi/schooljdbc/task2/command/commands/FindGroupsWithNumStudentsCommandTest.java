package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolderBuilder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcGroupDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Group;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = FindGroupsWithNumStudentsCommandTest.class)
class FindGroupsWithNumStudentsCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private FindGroupsWithNumStudentsCommand findGroupsWithNumStudentsCommand;
    @MockBean
    private JdbcGroupDao jdbcGroupDao;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        findGroupsWithNumStudentsCommand = new FindGroupsWithNumStudentsCommand(jdbcGroupDao);
        Group group = new Group(1, "test");
        List<Group> groups = new ArrayList<>();
        groups.add(group);
        when(jdbcGroupDao.findGroupsWithNumStudents(isA(Integer.class))).thenReturn(groups);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void findGroupsWithNumStudentsTest() {
        CommandHolder commandHolder = new CommandHolderBuilder();
        commandHolder.setNumStudents(1);
        findGroupsWithNumStudentsCommand.execute(commandHolder);
        assertEquals("""
                Groups with less or equal than 1 students
                Group ID:  1 | Group name:  test""", outputStreamCaptor.toString().trim());
    }
}