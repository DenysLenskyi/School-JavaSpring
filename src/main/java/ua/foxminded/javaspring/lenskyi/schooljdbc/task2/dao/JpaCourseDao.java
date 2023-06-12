package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;

import java.util.List;

@Repository
public class JpaCourseDao extends JpaBaseDao {

    public static final String FIND_BY_ID = "select c from Course c where c.id = :courseId";
    public static final String FIND_BY_NAME = "select c from Course c where c.name = :courseName";
    private static final String SELECT_MAX_COURSE_ID =
            "select max(c.id) from Course c";
    private static final String SELECT_MIN_COURSE_ID =
            "select min(c.id) from Course c";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void addCourses(List<Course> courses) {
        courses.forEach(entityManager::persist);
    }

    public Course findCourseById(long courseId) {
        return entityManager.createQuery(FIND_BY_ID, Course.class)
                .setParameter("courseId", courseId)
                .getSingleResult();
    }

    public boolean doesCourseExist(String courseName) {
        try {
            entityManager.createQuery(FIND_BY_NAME, Course.class)
                    .setParameter("courseName", courseName)
                    .getSingleResult();
            return true;
        } catch (EntityNotFoundException | EmptyResultDataAccessException | NoResultException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Long getMinCourseId() {
        try {
            return entityManager.createQuery(SELECT_MIN_COURSE_ID, Long.class).getSingleResult();
        } catch (PersistenceException e) {
            return 0L;
        }
    }

    public Long getMaxCourseId() {
        try {
            return entityManager.createQuery(SELECT_MAX_COURSE_ID, Long.class).getSingleResult();
        } catch (PersistenceException e) {
            return 0L;
        }
    }
}