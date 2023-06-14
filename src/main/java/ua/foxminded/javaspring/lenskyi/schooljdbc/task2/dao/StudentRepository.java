package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select min(s.id) from Student s")
    Long getMinStudentId();

    @Query("select max(s.id) from Student s")
    Long getMaxStudentId();
}