package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaBaseDao {

    @PersistenceContext
    protected EntityManager entityManager;

    @Transactional
    public void executeQuery(String sql) {
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}