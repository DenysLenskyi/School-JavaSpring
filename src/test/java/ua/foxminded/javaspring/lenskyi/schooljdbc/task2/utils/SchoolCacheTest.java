package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class SchoolCacheTest {

    @Autowired
    private SchoolCache schoolCache;

    @BeforeEach
    void setupCache() {
        schoolCache.setMinCourseId(1);
        schoolCache.setMaxCourseId(2);
        schoolCache.setMinGroupId(1);
        schoolCache.setMaxGroupId(2);
        schoolCache.setMinStudentId(1);
        schoolCache.setMaxStudentId(100);
    }

    @Test
    void getMinCourseIdTest() {
        long minCourseId = schoolCache.getMinCourseId();
        assertEquals(1, minCourseId);
    }

    @Test
    void getMaxCourseIdTest() {
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
        assertEquals(100, maxStudentId);
    }
}