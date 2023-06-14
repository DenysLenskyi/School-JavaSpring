package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@Transactional
@Order(3)
class StudentCourseRepositoryTest {

    @Autowired
    private StudentCourseRepository studentCourseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

//    @Test
//    @Sql(statements = )
//    void doesStudentVisitTheCourseTrueTest() {
//        long studentId = studentRepository.getMinStudentId() + 2;
//        final String insertIntoSchoolStudent =
//                "insert into school.student_course (student_id, course_id) values ("
//                        + studentId + "," + courseRepository.getMinCourseId() + ")";
//        studentCourseRepository.executeQuery(insertIntoSchoolStudent);
//        assertTrue(studentCourseRepository.isStudentEnrolledToCourse(studentId,
//                courseRepository.findById(courseRepository.getMinCourseId()).get().getName()));
//    }

    @Test
    void doesStudentVisitTheCourseFalseTest() {
        assertFalse(studentCourseRepository.isStudentEnrolledToCourse(studentRepository.getMaxStudentId() + 1,
                "Math"));
    }
}