package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

@Component
public class EnrollStudentToCourseCommand implements Command {

    private static final String STUDENT_ENROLLED_TO_COURSE = "Student enrolled to the course";
    private static final String CAN_NOT_ENROLL = "Can't perform operation...";
    private static final String WRONG_STUDENT_ID = "Wrong student's id. This id doesn't exist in the database";
    private static final String STUDENT_ALREADY_ENROLLED = "This student already visits this course";
    private static final String WRONG_COURSE_NAME = """
            Wrong course name.
            Available courses: Math, English, Biologic, Geography, Chemistry,
                               Physics, History, Finance, Sports, Etiquette.
            """;

    private JdbcStudentCourseDao jdbcStudentCoursesDao;
    private JdbcStudentDao jdbcStudentDao;
    private JdbcCourseDao jdbcCourseDao;

    @Autowired
    public EnrollStudentToCourseCommand(JdbcStudentCourseDao jdbcStudentCoursesDao,
                                        JdbcStudentDao jdbcStudentDao, JdbcCourseDao jdbcCourseDao) {
        this.jdbcStudentCoursesDao = jdbcStudentCoursesDao;
        this.jdbcStudentDao = jdbcStudentDao;
        this.jdbcCourseDao = jdbcCourseDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        if (!(jdbcStudentDao.doesStudentExist(commandHolder.getStudentId()))) {
            System.out.println(WRONG_STUDENT_ID);
            return;
        }
        if (!(jdbcCourseDao.isCourseExists(commandHolder.getCourseName()))) {
            System.out.println(WRONG_COURSE_NAME);
            return;
        }
        if (jdbcStudentCoursesDao.isStudentEnrolledToCourse(commandHolder.getStudentId(),
                commandHolder.getCourseName())) {
            System.out.println(STUDENT_ALREADY_ENROLLED);
        } else {
            jdbcStudentCoursesDao.addStudentToCourse(commandHolder.getStudentId(), commandHolder.getCourseName());
            System.out.println(STUDENT_ENROLLED_TO_COURSE);
        }
    }
}