package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

@Component
public class RemoveStudentFromCourseCommand implements Command {

    private static final String STUDENT_REMOVED_FROM_COURSE = "Student removed from the course";
    private static final String WRONG_STUDENT_ID = "Wrong student's id. This id doesn't exist in the database";
    private static final String STUDENT_ALREADY_REMOVED = "This student doesn't visit this course";
    private static final String WRONG_COURSE_NAME = """
            Wrong course name.
            Available courses: Math, English, Biologic, Geography, Chemistry,
                               Physics, History, Finance, Sports, Etiquette.
            """;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private JdbcStudentCourseDao jdbcStudentCoursesDao;
    private JdbcStudentDao jdbcStudentDao;
    private JdbcCourseDao jdbcCourseDao;

    @Autowired
    public RemoveStudentFromCourseCommand(JdbcStudentCourseDao jdbcStudentCoursesDao,
                                          JdbcStudentDao jdbcStudentDao, JdbcCourseDao jdbcCourseDao) {
        this.jdbcStudentCoursesDao = jdbcStudentCoursesDao;
        this.jdbcStudentDao = jdbcStudentDao;
        this.jdbcCourseDao = jdbcCourseDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        if (!(jdbcStudentDao.doesStudentExist(commandHolder.getStudentId()))) {
            System.out.println(WRONG_STUDENT_ID);
            log.warn("Attempt to remove non existed student with id={} from a course", commandHolder.getStudentId());
            return;
        }
        if (!(jdbcCourseDao.doesCourseExist(commandHolder.getCourseName()))) {
            System.out.println(WRONG_COURSE_NAME);
            log.warn("Attempt to remove student from non existed course with name = {}", commandHolder.getCourseName());
            return;
        }
        if (!(jdbcStudentCoursesDao.isStudentEnrolledToCourse(commandHolder.getStudentId(),
                commandHolder.getCourseName()))) {
            System.out.println(STUDENT_ALREADY_REMOVED);
            log.warn("Attempt to remove a student from the course they aren't visiting");
        } else {
            jdbcStudentCoursesDao.removeStudentFromCourse(commandHolder.getStudentId(), commandHolder.getCourseName());
            System.out.println(STUDENT_REMOVED_FROM_COURSE);
            log.info("Student with id={} removed from the course {}", commandHolder.getStudentId(),
                    commandHolder.getCourseName());
        }
    }
}