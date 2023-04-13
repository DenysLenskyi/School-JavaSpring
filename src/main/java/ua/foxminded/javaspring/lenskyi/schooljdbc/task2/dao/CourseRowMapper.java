package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;

public class CourseRowMapper implements RowMapper<Course> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt(ID));
        course.setName(rs.getString(NAME));
        course.setDescription(rs.getString(DESCRIPTION));
        return course;
    }
}