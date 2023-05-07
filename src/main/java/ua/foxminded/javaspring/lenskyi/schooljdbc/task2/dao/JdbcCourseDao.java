package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.CourseRowMapper;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcCourseDao extends JdbcBaseDao {

    public static final String INSERT_INTO_COURSE = "insert into school.course (name, description) values (?, ?);";
    public static final String FIND_BY_ID = "select * from school.course where id = ?";
    public static final String FIND_BY_NAME = "select * from school.course where name =?";

    @Autowired
    public JdbcCourseDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Transactional
    public void addCourses(List<Course> courses) {
        jdbcTemplate.batchUpdate(INSERT_INTO_COURSE,
                courses,
                10,
                (PreparedStatement ps, Course course) -> {
                    ps.setString(1, course.getName());
                    ps.setString(2, course.getDescription());
                });
    }

    public Course findCourseById(int courseId) {
        return jdbcTemplate.queryForObject(FIND_BY_ID, new CourseRowMapper(), courseId);
    }

    public boolean isCourseExists(String courseName) {
        Course course;
        course = jdbcTemplate.queryForObject(FIND_BY_NAME, new CourseRowMapper(), courseName);
        return course != null ? true : false;
    }
}