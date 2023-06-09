package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandCorrelationTest {

    @InjectMocks
    CommandCorrelation commandDefendant;

    @Mock
    private InfoCommand infoCommand;
    @Mock
    private UnknownCommand unknownCommand;
    @Mock
    private FindCourseByIdCommand findCourseByIdCommand;
    @Mock
    private FindGroupsWithNumStudentsCommand findGroupsWithNumStudentsCommand;
    @Mock
    private FindStudentsEnrolledToCourseCommand findStudentsEnrolledToCourseCommand;
    @Mock
    private AddStudentCommand addStudentCommand;
    @Mock
    private DeleteStudentCommand deleteStudentCommand;
    @Mock
    private EnrollStudentToCourseCommand enrollStudentToCourseCommand;
    @Mock
    private RemoveStudentFromCourseCommand removeStudentFromCourseCommand;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ValueSource(strings = {"info", "find_course", "find_groups", "find_students_course", "add_student",
            "delete_student", "add_student_course", "delete_student_course"})
    void getCommandCodeTest(String string) {
        assertEquals(commandDefendant.getCommandByCode(string), commandDefendant.getCommandCode().get(string));
    }
}