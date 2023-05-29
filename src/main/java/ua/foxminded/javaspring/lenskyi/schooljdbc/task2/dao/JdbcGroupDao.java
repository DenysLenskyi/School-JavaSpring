package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Group;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.GroupRowMapper;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcGroupDao extends JdbcBaseDao {

    public static final String INSERT_INTO_GROUP = "insert into school.group (id, name) values (default, ?)";
    private static final String FIND_GROUPS_WITH_LESS_OR_EQUAL_NUM_STUDENTS_QUERY = """
            select group_id id, school.group.name name
            from school.student
            inner join school.group on group_id = school.group.id
            group by name, group_id
            having count(student.id) <=?;
            """;

    @Autowired
    public JdbcGroupDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public void addGroups(List<Group> groups) {
        jdbcTemplate.batchUpdate(INSERT_INTO_GROUP,
                groups,
                10,
                (PreparedStatement ps, Group group) -> {
                    ps.setString(1, group.getName());
                });
    }

    public List<Group> findGroupsWithNumStudents (int numStudents) {
        return jdbcTemplate.query(FIND_GROUPS_WITH_LESS_OR_EQUAL_NUM_STUDENTS_QUERY,
                new GroupRowMapper(), numStudents);
    }
}