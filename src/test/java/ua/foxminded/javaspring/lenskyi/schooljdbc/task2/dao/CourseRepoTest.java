package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class CourseRepoTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findByIdTest() {
        Course course = courseRepository.findById(1L).get();
        assertEquals("Math", course.getName());
    }
}