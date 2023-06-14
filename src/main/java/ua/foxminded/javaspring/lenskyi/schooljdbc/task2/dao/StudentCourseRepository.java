package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.StudentCourse;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    @Query("""
                        select distinct s from Student s
                            join s.studentCourse sc
                            join sc.course c
                            where c.name = :courseName
            """)
    List<Student> getStudentsEnrolledToCourse(@Param("courseName") String courseName);

    @Query("""
            select case when (count(s) > 0) then true else false end from Student s
                            join s.studentCourse sc
                            join sc.course c
                            where c.name = :courseName
                            and s.id = :studentId
            """)
    Boolean isStudentEnrolledToCourse(@Param("studentId") long studentId,
                                      @Param("courseName") String courseName);

    @Modifying
    @Query(value = """
                        insert into school.student_course (student_id, course_id)
                        select :studentId, c.id
                        from school.course c
                        where c.name = :courseName;
            """, nativeQuery = true)
    void addStudentToCourse(@Param("studentId") long studentId,
                            @Param("courseName") String courseName);

    @Modifying
    @Query(value = """
            delete from school.student_course where student_id = :studentId
            and course_id in (select id from school.course where name = :courseName);
            """, nativeQuery = true)
    void removeStudentFromCourse(@Param("studentId") long studentId,
                                 @Param("courseName") String courseName);
}