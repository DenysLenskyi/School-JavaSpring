package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.StudentCourses;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcStudentCoursesDao extends JdbcBaseDao {

    public static final String INSERT_INTO_STUDENT_COURSES =
            "insert into school.student_courses (student_id, course_id) values (?,?)";
    private static final String FIND_STUDENTS_ENROLLED_TO_COURSE_QUERY = """
                        select distinct s.id, first_name, last_name
                            from school.student_courses s_c
                            inner join school.course c on course_id = c.id
                            inner join school.student s on s_c.student_id = s.id
                            where c.name = ?
            """;

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

    public List<Student> getStudentsEnrolledToCourse(String courseName) {
        return jdbcTemplate.query(FIND_STUDENTS_ENROLLED_TO_COURSE_QUERY, new StudentRowMapper(), courseName);
    }
}