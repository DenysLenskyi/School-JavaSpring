package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class DeleteStudentCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Autowired
    private DeleteStudentCommand deleteStudentCommand;
    @Autowired
    private JdbcStudentDao jdbcStudentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        List<Student> studentsBeforeDelete = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(studentsBeforeDelete.get(0).getId());
        //act
        deleteStudentCommand.execute(commandHolder);
        List<Student> studentsAfterDelete = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        //asserts
        assertEquals("Student deleted", outputStreamCaptor.toString().trim());
        assertEquals(4, studentsAfterDelete.size());
        jdbcStudentDao.executeQuery("INSERT INTO school.student (group_id, first_name, last_name) VALUES\n" +
                "    (null, 'Mark', 'Markson');");
    }

    @Test
    void deleteStudentNoSuchStudentIdTest() {
        //arranges
        List<Student> studentsBeforeDelete = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.setStudentId(1509375500);
        //act
        deleteStudentCommand.execute(commandHolder);
        List<Student> studentsAfterDelete = jdbcTemplate.query(
                "select * from school.student", new StudentRowMapper());
        //asserts
        assertEquals(studentsBeforeDelete.size(), studentsAfterDelete.size());
        assertEquals("Can't find this student id...", outputStreamCaptor.toString().trim());
    }
}