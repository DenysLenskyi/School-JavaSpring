package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class CommandCorrelationTest {

    @Autowired
    private CommandCorrelation commandCorrelation;

    @ParameterizedTest
    @ValueSource(strings = {"info", "find_course", "find_groups", "find_students_course", "add_student",
            "delete_student", "add_student_course", "delete_student_course"})
    void getCommandCodeTest(String string) {
        assertEquals("ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands",
                commandCorrelation.getCommandByCode(string).getClass().getPackageName());
    }
}