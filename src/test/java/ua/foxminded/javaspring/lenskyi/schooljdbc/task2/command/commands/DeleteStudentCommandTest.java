package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentDao;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class DeleteStudentCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Autowired
    private DeleteStudentCommand deleteStudentCommand;
    @Autowired
    private JpaStudentDao jpaStudentDao;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void deleteStudentCorrectTest() {
        //arranges
        long studentId = jpaStudentDao.getMaxStudentId() - 1;
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentId);
        //act
        deleteStudentCommand.execute(commandHolder);
        //asserts
        assertEquals("Student deleted", outputStreamCaptor.toString().trim());
        assertFalse(jpaStudentDao.doesStudentExist(studentId));
    }

    @Test
    void deleteStudentNoSuchStudentIdTest() {
        //arranges
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(jpaStudentDao.getMaxStudentId() + 1);
        //act
        deleteStudentCommand.execute(commandHolder);
        //asserts
        assertEquals("Can't find this student id...", outputStreamCaptor.toString().trim());
    }

    @Test
    void deleteStudentIncorrectInputTest() {
        deleteStudentCommand.execute(null);
        assertEquals("Student not deleted, check your input", outputStreamCaptor.toString().trim());
    }
}