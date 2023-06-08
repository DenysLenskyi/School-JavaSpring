package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Group;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaGroupDao extends JpaBaseDao {

    private static final String FIND_GROUPS_WITH_LESS_OR_EQUAL_NUM_STUDENTS_QUERY = """
            select g from Group g inner join g.students s 
            group by g.id, g.name
            having count(s.id) <= :numOfStudentId""";
    private static final String SELECT_MAX_GROUP_ID =
            "select max(g.id) from Group g";
    private static final String SELECT_MIN_GROUP_ID =
            "select min(g.id) from Group g";

    public void addGroups(List<Group> groups) {
        int batchSize = groups.size();
        for (int i = 0; i < groups.size(); i++) {
            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            entityManager.persist(groups.get(i));
        }
    }

    public List<Group> findGroupsWithNumStudents(int numStudents) {
        return entityManager.createQuery(FIND_GROUPS_WITH_LESS_OR_EQUAL_NUM_STUDENTS_QUERY, Group.class)
                .setParameter("numOfStudentId", numStudents)
                .getResultList();
    }

    public Optional<Long> getMinGroupId() {
        return Optional.ofNullable(entityManager.createQuery(SELECT_MIN_GROUP_ID, Long.class).getSingleResult());
    }

    public Optional<Long> getMaxGroupId() {
        return Optional.ofNullable(entityManager.createQuery(SELECT_MAX_GROUP_ID, Long.class).getSingleResult());
    }
}