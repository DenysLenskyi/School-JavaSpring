package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import jakarta.persistence.NoResultException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.StudentCourse;

import java.util.List;
import java.util.Set;

@Repository
public class JpaStudentCourseDao extends JpaBaseDao {

    public static final String INSERT_INTO_STUDENT_COURSES =
            "insert into school.student_course (student_id, course_id) values (?,?)";
    private static final String FIND_STUDENTS_ENROLLED_TO_COURSE_QUERY = """
                        select distinct s from Student s
                            join s.studentCourse sc
                            join sc.course c
                            where c.name = :courseName
            """;
    private static final String DELETE_STUDENT_COURSE = """
            delete from school.student_course where student_id = ?
            and course_id in (select id from school.course where name = ?);
            """;
    private static final String SELECT_BY_ID_AND_COURSE_NAME = """
            select distinct s from Student s
                            join s.studentCourse sc
                            join sc.course c
                            where c.name = :courseName
                            and s.id = :studentId
            """;

    private static final String ADD_STUDENT_TO_COURSE = """
                        insert into school.student_course (student_id, course_id)
                        select ?, c.id
                        from school.course c
                        where c.name = ?;
            """;

    public void addStudentsCourses(Set<StudentCourse> studentCourseSet) {
        studentCourseSet.forEach(studentCourse -> entityManager.createNativeQuery(INSERT_INTO_STUDENT_COURSES)
                .setParameter(1, studentCourse.getStudent().getId())
                .setParameter(2, studentCourse.getCourse().getId())
                .executeUpdate());
    }

    public List<Student> getStudentsEnrolledToCourse(String courseName) {
        return entityManager.createQuery(FIND_STUDENTS_ENROLLED_TO_COURSE_QUERY, Student.class)
                .setParameter("courseName", courseName)
                .getResultList();
    }

    public boolean isStudentEnrolledToCourse(long studentId, String courseName) {
        try {
            entityManager.createQuery(SELECT_BY_ID_AND_COURSE_NAME, Student.class)
                    .setParameter("courseName", courseName)
                    .setParameter("studentId", studentId)
                    .getSingleResult();
            return true;
        } catch (EmptyResultDataAccessException | NoResultException e) {
            return false;
        }

    }

    public void addStudentToCourse(long studentId, String courseName) {
        entityManager.createNativeQuery(ADD_STUDENT_TO_COURSE)
                .setParameter(1, studentId)
                .setParameter(2, courseName)
                .executeUpdate();
    }

    public void removeStudentFromCourse(long studentId, String courseName) {
        entityManager.createNativeQuery(DELETE_STUDENT_COURSE)
                .setParameter(1, studentId)
                .setParameter(2, courseName)
                .executeUpdate();
    }
}