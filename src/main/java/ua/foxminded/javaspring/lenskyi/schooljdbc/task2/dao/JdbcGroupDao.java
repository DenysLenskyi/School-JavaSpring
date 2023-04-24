package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Group;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcGroupDao extends JdbcBaseDao {

    public static final String INSERT_INTO_GROUP = "insert into school.group (id, name) values (default, ?)";

    @Autowired
    public JdbcGroupDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Transactional
    public void addGroups(List<Group> groups) {
        jdbcTemplate.batchUpdate(INSERT_INTO_GROUP,
                groups,
                10,
                (PreparedStatement ps, Group group) -> {
                    ps.setString(1, group.getName());
                });
    }
}
