package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.SchoolCache;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Group;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@Transactional
class SchoolCacheTest {

    @Autowired
    private SchoolCache schoolCache;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void getMinCourseIdTest() {
        long minCourseId = schoolCache.getMinCourseId();
        assertEquals(1, minCourseId);
    }

    @Test
    void getMaxCourseIdTest() {
        entityManager.persist(new Course("english", "english"));
        long maxCourseId = schoolCache.getMaxCourseId();
        assertEquals(2, maxCourseId);
    }

    @Test
    void getMinGroupIdTest() {
        long minGroupId = schoolCache.getMinGroupId();
        assertEquals(1, minGroupId);
    }

    @Test
    void getMaxGroupIdTest() {
        entityManager.persist(new Group("test"));
        long maxGroupId = schoolCache.getMaxGroupId();
        assertEquals(2, maxGroupId);
    }

    @Test
    void getMinStudentIdTest() {
        long minStudentId = schoolCache.getMinStudentId();
        assertEquals(1, minStudentId);
    }

    @Test
    void getMaxStudentIdTest() {
        long maxStudentId = schoolCache.getMaxStudentId();
        assertEquals(5, maxStudentId);
    }
}