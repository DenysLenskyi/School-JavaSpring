package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@Repository
public class JdbcStudentDao extends JdbcBaseDao {

    public static final String INSERT_INTO_STUDENT =
            "insert into school.student (group_id, first_name, last_name) values (?,?,?)";
    public static final String DELETE_STUDENT = "delete from school.student where id = ?";
    public static final String FIND_STUDENT_BY_ID = "select * from school.student where id = ?";

    @Autowired
    public JdbcStudentDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public void addStudents(List<Student> students) {
        jdbcTemplate.batchUpdate(INSERT_INTO_STUDENT,
                students,
                200,
                (PreparedStatement ps, Student student) -> {
                    if (student.getGroupId() == null) {
                        ps.setNull(1, Types.NULL);
                    } else {
                        ps.setLong(1, student.getGroupId());
                    }
                    ps.setString(2, student.getFirstName());
                    ps.setString(3, student.getLastName());
                });
    }

    public void addStudent(Long groupId, String firstName, String lastName) {
        jdbcTemplate.execute(
                INSERT_INTO_STUDENT,
                (PreparedStatementCallback<Boolean>) ps -> {
                    if (groupId == 0) {
                        ps.setNull(1, Types.NULL);
                    } else {
                        ps.setLong(1, groupId);
                    }
                    ps.setString(2, firstName);
                    ps.setString(3, lastName);
                    return ps.execute();
                });
    }

    public void deleteStudent(int studentId) {
        jdbcTemplate.execute(
                DELETE_STUDENT,
                (PreparedStatementCallback<Boolean>) ps -> {
                    ps.setInt(1, studentId);
                    return ps.execute();
                });
    }

    public Boolean doesStudentExist(int studentId) {
        try {
            jdbcTemplate.queryForObject(FIND_STUDENT_BY_ID, new StudentRowMapper(), studentId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}