package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.*;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcGroupDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCoursesDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandDefendantTest {

    private JdbcTemplate jdbcTemplate;
    private CommandDefendant commandDefendant = new CommandDefendant(
            new InfoCommand(), new UnknownCommand(), new FindCourseByIdCommand(new JdbcCourseDao(jdbcTemplate)),
            new FindGroupsWithNumStudentsCommand(new JdbcGroupDao(jdbcTemplate)),
            new FindStudentsEnrolledToCourseCommand(new JdbcStudentCoursesDao(jdbcTemplate),
                    new JdbcCourseDao(jdbcTemplate)),
            new AddStudentCommand(new JdbcStudentDao(jdbcTemplate)),
            new DeleteStudentCommand(new JdbcStudentDao(jdbcTemplate)),
            new EnrollStudentToCourseCommand(new JdbcStudentCoursesDao(jdbcTemplate),
                    new JdbcStudentDao(jdbcTemplate), new JdbcCourseDao(jdbcTemplate)),
            new RemoveStudentFromCourseCommand(new JdbcStudentCoursesDao(jdbcTemplate),
                    new JdbcStudentDao(jdbcTemplate), new JdbcCourseDao(jdbcTemplate))
    );

    @ParameterizedTest
    @ValueSource(strings = {"info", "find_course", "find_groups", "find_students_course", "add_student",
            "delete_student", "add_student_course", "delete_student_course"})
    void getCommandCodeTest(String string) {
        assertEquals(commandDefendant.getCommandByCode(string), commandDefendant.getCommandCode().get(string));
    }
}