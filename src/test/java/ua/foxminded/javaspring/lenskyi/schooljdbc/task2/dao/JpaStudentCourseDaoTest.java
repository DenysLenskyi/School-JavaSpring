package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Order;
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
@Transactional
@Order(3)
class JpaStudentCourseDaoTest {

    @Autowired
    private JpaStudentCourseDao jpaStudentCourseDao;
    @Autowired
    private JpaStudentDao jpaStudentDao;
    @Autowired
    private JpaCourseDao jpaCourseDao;

    @Test
    void doesStudentVisitTheCourseTrueTest() {
        long studentId = jpaStudentDao.getMinStudentId() + 2;
        final String insertIntoSchoolStudent =
                "insert into school.student_course (student_id, course_id) values ("
                        + studentId + "," + jpaCourseDao.getMinCourseId() + ")";
        jpaStudentCourseDao.executeQuery(insertIntoSchoolStudent);
        assertTrue(jpaStudentCourseDao.isStudentEnrolledToCourse(studentId,
                jpaCourseDao.findCourseById(jpaCourseDao.getMinCourseId()).getName()));
    }

    @Test
    void doesStudentVisitTheCourseFalseTest() {
        assertFalse(jpaStudentCourseDao.isStudentEnrolledToCourse(jpaStudentDao.getMaxStudentId() + 1,
                "Math"));
    }
}