package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcStudentDao extends JdbcBaseDao {

    public static final String INSERT_INTO_STUDENT =
            "insert into school.student (group_id, first_name, last_name) values (?,?,?)";

    @Autowired
    public JdbcStudentDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Transactional
    public void addStudents(List<Student> students) {
        jdbcTemplate.batchUpdate(INSERT_INTO_STUDENT,
                students,
                200,
                (PreparedStatement ps, Student student) -> {
                    if (student.getGroupId() == 0) {
                        ps.setNull(1, student.getGroupId());
                    } else {
                        ps.setInt(1, student.getGroupId());
                    }
                    ps.setString(2, student.getFirstName());
                    ps.setString(3, student.getLastName());
                });
    }
}