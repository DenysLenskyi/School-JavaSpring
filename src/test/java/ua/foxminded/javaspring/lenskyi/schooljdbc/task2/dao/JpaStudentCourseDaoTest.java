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
class JpaStudentCourseDaoTest {

    @Autowired
    private JpaStudentCourseDao jpaStudentCourseDao;
    @Autowired
    private JpaStudentDao jpaStudentDao;
    @Autowired
    private JpaCourseDao jpaCourseDao;

    @Test
    void doesStudentVisitTheCourseTrueTest() {
        final String insertIntoSchoolStudent =
                "insert into school.student_course (student_id, course_id) values ("
                        + jpaStudentDao.getMinStudentId() + "," + jpaCourseDao.getMinCourseId() + ")";
        jpaStudentCourseDao.executeQuery(insertIntoSchoolStudent);
        assertTrue(jpaStudentCourseDao.isStudentEnrolledToCourse(jpaStudentDao.getMinStudentId().get(), "Math"));
    }

    @Test
    void doesStudentVisitTheCourseFalseTest() {
        assertFalse(jpaStudentCourseDao.isStudentEnrolledToCourse(jpaStudentDao.getMaxStudentId().get() + 1,
                "Math"));
    }
}