package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {

    private static final String ID = "id";
    private static final String GROUP_ID = "group_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt(ID));
        student.setGroupId(rs.getLong(GROUP_ID));
        student.setFirstName(rs.getString(FIRST_NAME));
        student.setLastName(rs.getString(LAST_NAME));
        return student;
    }
}
