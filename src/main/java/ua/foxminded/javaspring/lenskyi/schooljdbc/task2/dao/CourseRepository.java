package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Boolean existsByName(String name);

    @Query("select min(c.id) from Course c")
    Long getMinCourseId();

    @Query("select max (c.id) from Course c")
    Long getMaxCourseId();
}