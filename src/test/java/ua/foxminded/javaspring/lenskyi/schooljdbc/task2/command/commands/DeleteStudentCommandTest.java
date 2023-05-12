package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest(classes = DeleteStudentCommand.class)
class DeleteStudentCommandTest {

    private DeleteStudentCommand deleteStudentCommand;
    @MockBean
    private JdbcStudentDao jdbcStudentDao;

    @BeforeEach
    void setUp() {
        deleteStudentCommand = new DeleteStudentCommand(jdbcStudentDao);
        doCallRealMethod().when(jdbcStudentDao).deleteStudent(isA(Integer.class));
        when(jdbcStudentDao.doesStudentExist(isA(Integer.class))).thenReturn(true);
    }

    @Test
    void deleteStudentTest() {
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(1);
        deleteStudentCommand.execute(commandHolder);
        verify(jdbcStudentDao, times(1)).deleteStudent(1);
    }
}