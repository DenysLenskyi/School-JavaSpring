package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SchoolCache extends JpaBaseDao {

    private static final String SELECT_MAX_COURSE_ID =
            "select max(c.id) from Course c";
    private static final String SELECT_MAX_GROUP_ID =
            "select max(g.id) from Group g";
    private static final String SELECT_MAX_STUDENT_ID =
            "select max(s.id) from Student s";
    private static final String SELECT_MIN_COURSE_ID =
            "select min(c.id) from Course c";
    private static final String SELECT_MIN_GROUP_ID =
            "select min(g.id) from Group g";
    private static final String SELECT_MIN_STUDENT_ID =
            "select min(s.id) from Student s";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public long getMinCourseId() {
        try {
            Long minCourseId = entityManager.createQuery(SELECT_MIN_COURSE_ID, Long.class).getSingleResult();
            if (minCourseId != null) {
                return minCourseId;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    public long getMaxCourseId() {
        try {
            Long maxCourseId = entityManager.createQuery(SELECT_MAX_COURSE_ID, Long.class).getSingleResult();
            if (maxCourseId != null) {
                return maxCourseId;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    public long getMinGroupId() {
        try {
            Long minGroupId = entityManager.createQuery(SELECT_MIN_GROUP_ID, Long.class).getSingleResult();
            if (minGroupId != null) {
                return minGroupId;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    public long getMaxGroupId() {
        try {
            Long maxGroupId = entityManager.createQuery(SELECT_MAX_GROUP_ID, Long.class).getSingleResult();
            if (maxGroupId != null) {
                return maxGroupId;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    public long getMinStudentId() {
        try {
            Long minStudentId = entityManager.createQuery(SELECT_MIN_STUDENT_ID, Long.class).getSingleResult();
            if (minStudentId != null) {
                return minStudentId;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    public long getMaxStudentId() {
        try {
            Long maxStudentId = entityManager.createQuery(SELECT_MAX_STUDENT_ID, Long.class).getSingleResult();
            if (maxStudentId != null) {
                return maxStudentId;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }
}