package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Group;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.StudentCourse;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomDataCreatorTest {

    private Random random;
    private FileReader reader;
    private RandomDataCreator randomDataCreator;

    @BeforeEach
    public void setup() {
        random = new Random();
        reader = new FileReader();
        randomDataCreator = new RandomDataCreator(reader, random);
        randomDataCreator.setCOURSES("/courses.txt");
        randomDataCreator.setNAMES("/names.txt");
    }


    @ParameterizedTest
    @MethodSource("getArgsForGetCoursesTest")
    void getCoursesFromResourcesTest(int elementNumInList, String expectedCourseName) {
        List<Course> courses = randomDataCreator.getCoursesFromResources();
        assertEquals(courses.get(elementNumInList).getName(), expectedCourseName);
    }

    private static Stream<Arguments> getArgsForGetCoursesTest() {
        return Stream.of(
                Arguments.of(0, "Math"),
                Arguments.of(1, "English"),
                Arguments.of(2, "Biologic"),
                Arguments.of(3, "Geography"),
                Arguments.of(4, "Chemistry"),
                Arguments.of(5, "Physics"),
                Arguments.of(6, "History"),
                Arguments.of(7, "Finance"),
                Arguments.of(8, "Sports"),
                Arguments.of(9, "Etiquette")
        );

    }

    @Test
    void generateGroupsTest() {
        List<Group> groups = randomDataCreator.generateGroups(10);
        assertTrue(groups.size() == 10);
    }

    @Test
    void generateStudentsTest() {
        List<Student> students = randomDataCreator.generateStudents(10);
        assertTrue(students.get(9).getFirstName().length() > 1);
    }

    @Test
    void generateStudentCourseTest() {
        List<StudentCourse> studentCourses = randomDataCreator.generateStudentCourseRelation(200);
        assertTrue(studentCourses.size() > 199);
        assertTrue(studentCourses.get(10).getCourseId() > 0);
        assertTrue(studentCourses.get(10).getStudentId() > 0);
    }
}