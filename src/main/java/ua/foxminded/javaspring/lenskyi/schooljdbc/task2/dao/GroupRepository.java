package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Group;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("""
            select g from Group g inner join g.students s 
            group by g.id, g.name
            having count(s.id) <= :numOfStudentId""")
    List<Group> findGroupsWithNumStudents(@Param("numOfStudentId") int numStudents);

    @Query("select min(g.id) from Group g")
    Long getMinGroupId();

    @Query("select max(g.id) from Group g")
    Long getMaxGroupId();
}