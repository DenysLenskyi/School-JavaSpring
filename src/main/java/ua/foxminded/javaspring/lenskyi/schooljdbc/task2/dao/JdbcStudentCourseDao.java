package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.StudentCourse;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.rowMapper.StudentRowMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcStudentCourseDao extends JdbcBaseDao {

    public static final String INSERT_INTO_STUDENT_COURSES =
            "insert into school.student_course (student_id, course_id) values (?,?)";
    private static final String FIND_STUDENTS_ENROLLED_TO_COURSE_QUERY = """
                        select distinct s.id, s.group_id, first_name, last_name
                            from school.student_course s_c
                            inner join school.course c on course_id = c.id
                            inner join school.student s on s_c.student_id = s.id
                            where c.name = ?
            """;
    private static final String DELETE_STUDENT_COURSE = """
            delete from school.student_course where student_id = ?
            and course_id in (select id from school.course where name = ?);
            """;
    private static final String SELECT_BY_ID_AND_COURSE_NAME = """
            select * from school.student_course 
            where student_id = ? and course_id in (select
            id from school.course where name = ?)
            """;

    private static final String ADD_STUDENT_TO_COURSE = """
                        insert into school.student_course (student_id, course_id)
                        select ?, c.id
                        from school.course c
                        where c.name = ?;
            """;

    @Autowired
    public JdbcStudentCourseDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public void addStudentsCourses(List<StudentCourse> studentCourseList) {
        jdbcTemplate.batchUpdate(INSERT_INTO_STUDENT_COURSES,
                studentCourseList,
                studentCourseList.size(),
                (PreparedStatement ps, StudentCourse studentCourse) -> {
                    ps.setInt(1, studentCourse.getStudentId());
                    ps.setInt(2, studentCourse.getCourseId());
                });
    }

    public List<Student> getStudentsEnrolledToCourse(String courseName) {
        return jdbcTemplate.query(FIND_STUDENTS_ENROLLED_TO_COURSE_QUERY, new StudentRowMapper(), courseName);
    }

    public boolean isStudentEnrolledToCourse(int studentId, String courseName) {
        List<Map<String, Object>> studentCourse =
                jdbcTemplate.queryForList(SELECT_BY_ID_AND_COURSE_NAME, studentId, courseName);
        return !studentCourse.isEmpty();
    }

    public void addStudentToCourse(int studentId, String courseName) {
        jdbcTemplate.execute(
                ADD_STUDENT_TO_COURSE,
                (PreparedStatementCallback<Boolean>) ps -> {
                    ps.setInt(1, studentId);
                    ps.setString(2, courseName);
                    return ps.execute();
                });
    }

    public void removeStudentFromCourse(int studentId, String courseName) {
        jdbcTemplate.execute(
                DELETE_STUDENT_COURSE,
                (PreparedStatementCallback<Boolean>) ps -> {
                    ps.setInt(1, studentId);
                    ps.setString(2, courseName);
                    return ps.execute();
                });
    }
}