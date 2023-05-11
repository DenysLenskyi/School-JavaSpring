package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = AddStudentCommand.class)
class AddStudentCommandTest1 {

    @MockBean
    private AddStudentCommand addStudentCommand;

    @Test
    void addStudentTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setGroupId(1L);
        commandHolder.setStudentFirstName("Mark");
        commandHolder.setStudentLastName("Mark");
        doCallRealMethod().when(addStudentCommand).execute(commandHolder);
        addStudentCommand.execute(commandHolder);
        verify(addStudentCommand, times(1)).execute(commandHolder);
    }
}