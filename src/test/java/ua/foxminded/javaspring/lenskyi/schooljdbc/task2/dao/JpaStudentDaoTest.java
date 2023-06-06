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
class JpaStudentDaoTest {

    @Autowired
    private JpaStudentDao jpaStudentDao;
    @Autowired
    private SchoolCache schoolCache;

    @Test
    void doesStudentExistTrueTest() {
        assertTrue(jpaStudentDao.doesStudentExist(schoolCache.getMinStudentId()));
    }

    @Test
    void doesStudentExistFalseTest() {
        assertFalse(jpaStudentDao.doesStudentExist(schoolCache.getMaxStudentId() + 1));
    }
}