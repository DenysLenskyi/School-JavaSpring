package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CommandHolderBuilderTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "info",
            "find_course --course_id=7",
            "find_groups --num_students=20",
            "find_students --course_name=History",
            "add_student --group_id=10 --first_name=Alice --last_name=Cooper",
            "delete_student --student_id=1",
            "add_student_course --student_id=1 --course_name=History",
            "delete_student_course --student_id=1 --course_name=History"
    })
    void buildCommandFromInputStringCorrectCommandNameTest(String string) {
        CommandHolder test = CommandHolderBuilder.buildCommandFromInputString(string);
        String[] str = string.split(" ");
        assertTrue(test.getCommandName().equals(str[0]));
    }

    @Test
    void invokeFindCourseTest() {
        CommandHolder test = CommandHolderBuilder
                .buildCommandFromInputString("find_course --course_id=7");
        assertEquals(7, test.getCourseId());
    }

    @Test
    void invokeFindGroupTest() {
        CommandHolder test = CommandHolderBuilder
                .buildCommandFromInputString("find_groups --num_students=20");
        assertEquals(20, test.getNumStudents());
    }

    @Test
    void invokeStudentsFromCourseTest() {
        CommandHolder test = CommandHolderBuilder
                .buildCommandFromInputString("find_students --course_name=History");
        assertEquals("History", test.getCourseName());
    }

    @Test
    void invokeAddStudentTest() {
        CommandHolder test = CommandHolderBuilder
                .buildCommandFromInputString("add_student --group_id=10 --first_name=Alice --last_name=Cooper");
        assertEquals(10, test.getGroupId());
        assertEquals("Alice", test.getStudentFirstName());
        assertEquals("Cooper", test.getStudentLastName());
    }

    @Test
    void invokeDeleteStudentTest() {
        CommandHolder test = CommandHolderBuilder
                .buildCommandFromInputString("delete_student --student_id=1");
        assertEquals(1, test.getStudentId());
    }

    @Test
    void invokeAddStudentToCourseTest() {
        CommandHolder test = CommandHolderBuilder
                .buildCommandFromInputString("add_student_course --student_id=1 --course_name=History");
        assertEquals(1, test.getStudentId());
        assertEquals("History", test.getCourseName());
    }

    @Test
    void invokeDeleteStudentFromCourseTest() {
        CommandHolder test = CommandHolderBuilder
                .buildCommandFromInputString("delete_student_course --student_id=1 --course_name=History");
        assertEquals(1, test.getStudentId());
        assertEquals("History", test.getCourseName());
    }
}