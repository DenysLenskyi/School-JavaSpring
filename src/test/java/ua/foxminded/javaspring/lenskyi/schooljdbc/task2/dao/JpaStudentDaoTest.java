package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.SchoolJDBCCache;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class JpaStudentDaoTest {

    @Autowired
    JpaStudentDao jpaStudentDao;
    @Autowired
    SchoolJDBCCache schoolJDBCCache;

    @Test
    void doesStudentExistTrueTest() {
        assertTrue(jpaStudentDao.doesStudentExist(schoolJDBCCache.getMinStudentId()));
    }

    @Test
    void doesStudentExistFalseTest() {
        assertFalse(jpaStudentDao.doesStudentExist(schoolJDBCCache.getMaxStudentId() + 1));
    }
}