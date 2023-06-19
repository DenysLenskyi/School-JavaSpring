package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select min(s.id) from Student s")
    Long getMinStudentId();

    @Query("select max(s.id) from Student s")
    Long getMaxStudentId();

    @Query("select case when count(s)> 0 then true else false end from Student s join s.courses c " +
            "where s.id = :studentId and c.id = :courseId")
    Boolean doesStudentVisitTheCourse(@Param("studentId") Long studentId,
                                      @Param("courseId") Long courseId);

    @Query("select s from Student s join s.courses c where c.name = :courseName")
    List<Student> findStudentsEnrolledToCourse(@Param("courseName") String courseName);
}