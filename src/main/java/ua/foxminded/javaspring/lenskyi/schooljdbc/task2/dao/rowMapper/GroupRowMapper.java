package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRowMapper implements RowMapper<Group> {

    private static final String ID = "group_id";
    private static final String NAME = "group_name";

    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();
        group.setId(rs.getInt(ID));
        group.setName(rs.getString(NAME));
        return group;
    }
}