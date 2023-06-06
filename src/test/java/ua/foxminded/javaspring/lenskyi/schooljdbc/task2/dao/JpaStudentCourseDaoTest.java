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
    private SchoolCache schoolCache;

    @Test
    void doesStudentVisitTheCourseTrueTest() {
        final String insertIntoSchoolStudent =
                "insert into school.student_course (student_id, course_id) values ("
                        + schoolCache.getMinStudentId() + "," + schoolCache.getMinCourseId() + ")";
        jpaStudentCourseDao.executeQuery(insertIntoSchoolStudent);
        assertTrue(jpaStudentCourseDao.isStudentEnrolledToCourse(schoolCache.getMinStudentId(), "Math"));
        final String deleteFromStudentCourse =
                "delete from school.student_course where student_id =" + schoolCache.getMinStudentId();
    }

    @Test
    void doesStudentVisitTheCourseFalseTest() {
        assertFalse(jpaStudentCourseDao.isStudentEnrolledToCourse(1534869, "Math"));
    }
}