package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;

import java.util.List;

@Repository
public class JpaCourseDao extends JdbcBaseDao {

    public static final String FIND_BY_ID = "select c from Course c where c.id = :courseId";
    public static final String FIND_BY_NAME = "select c from Course c where c.name = :courseName";
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public JpaCourseDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Transactional
    public void addCourses(List<Course> courses) {
        int batchSize = courses.size();
        for (int i = 0; i < courses.size(); i++) {
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            entityManager.persist(courses.get(i));
        }
    }

    public Course findCourseById(int courseId) {
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
        } catch (EntityNotFoundException e) {
            return false;
        }
    }
}