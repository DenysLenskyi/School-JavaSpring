package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcBaseDao {

    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcBaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void executeQuery(String sql) {
        jdbcTemplate.execute(sql);
    }
}