package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void doesStudentExistTrueTest() {
        assertTrue(studentRepository.existsById(studentRepository.getMinStudentId()));
    }

    @Test
    void doesStudentExistFalseTest() {
        assertFalse(studentRepository.existsById(studentRepository.getMaxStudentId() + 1));
    }
}