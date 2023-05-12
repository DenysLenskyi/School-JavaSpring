package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest(classes = AddStudentCommand.class)
class AddStudentCommandTest {

    @MockBean
    private JdbcStudentDao jdbcStudentDao;
    private AddStudentCommand addStudentCommand;

    @BeforeEach
    void setup() {
        addStudentCommand = new AddStudentCommand(jdbcStudentDao);
        doNothing().when(jdbcStudentDao).addStudent(isA(Long.class), isA(String.class), isA(String.class));
    }

    @Test
    void addStudentTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(1L);
        commandHolder.setStudentFirstName("Mark");
        commandHolder.setStudentLastName("Mark");
        addStudentCommand.execute(commandHolder);
        verify(jdbcStudentDao, times(1)).addStudent(
                commandHolder.getGroupId(),
                commandHolder.getStudentFirstName(),
                commandHolder.getStudentLastName());
    }
}