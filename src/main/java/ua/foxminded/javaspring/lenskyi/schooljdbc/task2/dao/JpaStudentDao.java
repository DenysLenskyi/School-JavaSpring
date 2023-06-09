package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaStudentDao extends JpaBaseDao {

    public static final String INSERT_INTO_STUDENT =
            "insert into school.student (group_id, first_name, last_name) values (?,?,?)";
    public static final String FIND_STUDENT_BY_ID = "select s from Student s where s.id = :studentId";
    private static final String SELECT_MAX_STUDENT_ID =
            "select max(s.id) from Student s";
    private static final String SELECT_MIN_STUDENT_ID =
            "select min(s.id) from Student s";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void addStudents(List<Student> students) {
        students.forEach(entityManager::persist);
    }

    public void addStudent(Long groupId, String firstName, String lastName) {
        entityManager.createNativeQuery(INSERT_INTO_STUDENT)
                .setParameter(1, groupId)
                .setParameter(2, firstName)
                .setParameter(3, lastName)
                .executeUpdate();
    }

    public void deleteStudent(long studentId) {
        Student student = entityManager.find(Student.class, studentId);
        entityManager.remove(student);
        entityManager.flush();
        entityManager.clear();
    }

    public Boolean doesStudentExist(long studentId) {
        try {
            entityManager.createQuery(FIND_STUDENT_BY_ID, Student.class)
                    .setParameter("studentId", studentId)
                    .getSingleResult();
            return true;
        } catch (EntityNotFoundException | EmptyResultDataAccessException | NoResultException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Optional<Long> getMinStudentId() {
        try {
            return Optional.of(entityManager.createQuery(SELECT_MIN_STUDENT_ID, Long.class).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Long> getMaxStudentId() {
        return Optional.of(entityManager.createQuery(SELECT_MAX_STUDENT_ID, Long.class).getSingleResult());
    }
}