package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Group;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.StudentCourse;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class RandomDataCreatorTest {

    private Random random;
    private FileReader reader;
    @Autowired
    private RandomDataCreator randomDataCreator;

//    @BeforeEach
//    public void setup() {
//        random = new Random();
//        reader = new FileReader();
//        randomDataCreator = new RandomDataCreator(reader, random);
//        randomDataCreator.setCOURSES("/courses.txt");
//        randomDataCreator.setNAMES("/names.txt");
//    }


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
        Set<StudentCourse> studentCourses = randomDataCreator.generateStudentCourseRelation(200);
        assertTrue(studentCourses.size() > 199);
        studentCourses.forEach(e ->{
            assertTrue(e.getStudentId() > 0);
        });
    }
}