package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class CommandCorrelationTest {

    @Autowired
    private CommandCorrelation commandCorrelation;

    @ParameterizedTest
    @MethodSource("getArgsForComCorTest")
    void getCommandCodeTest(String string, Class expectedClass) {
        commandCorrelation.getCommandByCode(string);
        assertTrue(commandCorrelation.getCommandByCode(string).getClass().getName().contains(expectedClass.getName()));
    }

    private static Stream<Arguments> getArgsForComCorTest() {
        return Stream.of(
                Arguments.of("info", InfoCommand.class),
                Arguments.of("find_course", FindCourseByIdCommand.class),
                Arguments.of("find_groups", FindGroupsWithNumStudentsCommand.class),
                Arguments.of("find_students_course", FindStudentsEnrolledToCourseCommand.class),
                Arguments.of("add_student", AddStudentCommand.class),
                Arguments.of("delete_student", DeleteStudentCommand.class),
                Arguments.of("add_student_course", EnrollStudentToCourseCommand.class),
                Arguments.of("delete_student_course", RemoveStudentFromCourseCommand.class),
                Arguments.of("accio_student!", UnknownCommand.class)
        );
    }
}