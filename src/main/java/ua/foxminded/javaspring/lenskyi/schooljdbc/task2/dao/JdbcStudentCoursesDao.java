package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.StudentCourses;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcStudentCoursesDao extends JdbcBaseDao {

    public static final String INSERT_INTO_STUDENT_COURSES =
            "insert into school.student_courses (student_id, course_id) values (?,?)";

    @Autowired
    public JdbcStudentCoursesDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Transactional
    public void addStudents(List<StudentCourses> studentCoursesList) {
        studentCoursesList.forEach(sc -> {
            sc.getCourseIds().forEach(courseId -> {
                jdbcTemplate.execute(INSERT_INTO_STUDENT_COURSES, new PreparedStatementCallback<Boolean>() {
                    @Override
                    public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException {
                        ps.setInt(1, sc.getStudentId());
                        ps.setInt(2, courseId);
                        return ps.execute();
                    }
                });
            });
        });
    }
}