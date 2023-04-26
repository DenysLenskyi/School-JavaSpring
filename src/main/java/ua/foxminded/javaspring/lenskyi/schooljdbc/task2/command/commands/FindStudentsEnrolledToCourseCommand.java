package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCoursesDao;

@Component
public class FindStudentsEnrolledToCourseCommand implements Command {

    private static final String STUDENT_ID = "Student ID:";
    private static final String STUDENT_FULL_NAME = "Student name:";
    private static final String FORMAT = "%1$s %2$s | %3$s %4$s %5$s";
    private static final String DISCLAIMER_AFTER_WRONG_INPUT = """
            Failed...
            Available courses: Math, English, Biologic, Geography, Chemistry,
                               Physics, History, Finance, Sports, Etiquette.
            """;

    private JdbcStudentCoursesDao jdbcStudentCoursesDao;
    private JdbcCourseDao jdbcCourseDao;

    @Autowired
    public FindStudentsEnrolledToCourseCommand(JdbcStudentCoursesDao jdbcStudentCoursesDao, JdbcCourseDao jdbcCourseDao) {
        this.jdbcStudentCoursesDao = jdbcStudentCoursesDao;
        this.jdbcCourseDao = jdbcCourseDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        try {
            if (jdbcCourseDao.isCourseExists(commandHolder.getCourseName())) {
                jdbcStudentCoursesDao.getStudentsEnrolledToCourse(commandHolder.getCourseName())
                        .stream()
                        .map(student -> String.format(FORMAT, STUDENT_ID, student.getId(),
                                STUDENT_FULL_NAME, student.getFirstName(), student.getLastName()))
                        .forEach(System.out::println);
                System.out.println('\n');
            }
        } catch (Exception e) {
            System.out.println(DISCLAIMER_AFTER_WRONG_INPUT);
        }
    }
}