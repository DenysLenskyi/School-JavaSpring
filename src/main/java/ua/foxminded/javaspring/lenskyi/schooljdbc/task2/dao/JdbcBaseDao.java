package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcBaseDao {

    protected JdbcTemplate jdbcTemplate;

    protected List<String> availableCourseNames = new ArrayList<>();

    @Autowired
    public JdbcBaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getAvailableCourseNames() {
        return availableCourseNames;
    }

    public void setAvailableCourseNames(List<String> availableCourseNames) {
        this.availableCourseNames = availableCourseNames;
    }

    public void executeQuery(String sql) {
        jdbcTemplate.execute(sql);
    }
}