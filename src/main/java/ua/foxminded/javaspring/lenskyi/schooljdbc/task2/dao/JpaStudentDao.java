package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;

import java.util.List;

@Repository
@Transactional
public class JpaStudentDao extends JpaBaseDao {

    public static final String INSERT_INTO_STUDENT =
            "insert into school.student (group_id, first_name, last_name) values (?,?,?)";
    public static final String FIND_STUDENT_BY_ID = "select s from Student s where s.id = :studentId";

    public void addStudents(List<Student> students) {
        int batchSize = students.size();
        for (int i = 0; i < students.size(); i++) {
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            entityManager.persist(students.get(i));
        }
    }

    public void addStudent(Long groupId, String firstName, String lastName) {
        entityManager.createNativeQuery(INSERT_INTO_STUDENT)
                .setParameter(1, groupId)
                .setParameter(2, firstName)
                .setParameter(3, lastName)
                .executeUpdate();
    }

    public void deleteStudent(int studentId) {
        Student student = entityManager.find(Student.class, studentId);
        entityManager.remove(student);
        entityManager.flush();
        entityManager.clear();
    }

    public Boolean doesStudentExist(int studentId) {
        try {
            entityManager.createQuery(FIND_STUDENT_BY_ID, Student.class)
                    .setParameter("studentId", studentId)
                    .getSingleResult();
            return true;
        } catch (EntityNotFoundException | EmptyResultDataAccessException | NoResultException e) {
            return false;
        }
    }
}