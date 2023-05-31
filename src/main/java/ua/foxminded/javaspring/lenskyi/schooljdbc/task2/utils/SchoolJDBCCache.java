package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Group;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.CourseRowMapper;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.GroupRowMapper;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;

@Component
public class SchoolJDBCCache {

    private static final String SELECT_MAX_COURSE_ID =
            "select * from school.course where id = (select max(id) from school.course);";
    private static final String SELECT_MAX_GROUP_ID =
            "select * from school.group where id = (select max(id) from school.group);";
    private static final String SELECT_MAX_STUDENT_ID =
            "select * from school.student where id = (select max(id) from school.student);";
    private static final String SELECT_MIN_COURSE_ID =
            "select * from school.course where id = (select min(id) from school.course);";
    private static final String SELECT_MIN_GROUP_ID =
            "select * from school.group where id = (select min(id) from school.group);";
    private static final String SELECT_MIN_STUDENT_ID =
            "select * from school.student where id = (select min(id) from school.student);";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SchoolJDBCCache(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getMinCourseId() {
        Course course = jdbcTemplate.queryForObject(SELECT_MIN_COURSE_ID, new CourseRowMapper());
        if (course != null) {
            return course.getId();
        } else {
            return 0;
        }
    }

    public int getMaxCourseId() {
        Course course = jdbcTemplate.queryForObject(SELECT_MAX_COURSE_ID, new CourseRowMapper());
        if (course != null) {
            return course.getId();
        } else {
            return 0;
        }
    }

    public int getMinGroupId() {
        Group group = jdbcTemplate.queryForObject(SELECT_MIN_GROUP_ID, new GroupRowMapper());
        if (group != null) {
            return group.getId();
        } else {
            return 0;
        }
    }

    public int getMaxGroupId() {
        Group group = jdbcTemplate.queryForObject(SELECT_MAX_GROUP_ID, new GroupRowMapper());
        if (group != null) {
            return group.getId();
        } else {
            return 0;
        }
    }

    public int getMinStudentId() {
        Student student = jdbcTemplate.queryForObject(SELECT_MIN_STUDENT_ID, new StudentRowMapper());
        if (student != null) {
            return student.getId();
        } else {
            return 0;
        }
    }

    public int getMaxStudentId() {
        Student student = jdbcTemplate.queryForObject(SELECT_MAX_STUDENT_ID, new StudentRowMapper());
        if (student != null) {
            return student.getId();
        } else {
            return 0;
        }
    }
}