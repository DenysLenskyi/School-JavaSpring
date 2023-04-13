package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;

@Component
public class FindCourseByIdCommand implements Command {

    private static final String COURSE_ID = "Course ID: ";
    private static final String COURSE_NAME = "Course name: ";
    private static final String COURSE_DESCRIPTION = "Description: ";
    private static final String FORMAT = "%1$s %2$s | %3$s %4$s | %5$s %6$s\n";

    private JdbcCourseDao jdbcCourseDao;

    @Autowired
    public FindCourseByIdCommand(JdbcCourseDao jdbcCourseDao) {
        this.jdbcCourseDao = jdbcCourseDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        Course course = jdbcCourseDao.findCourseById(commandHolder.getCourseId());
        String str = String.format(
                FORMAT, COURSE_ID, course.getId(), COURSE_NAME, course.getName(),
                COURSE_DESCRIPTION, course.getDescription());
        System.out.println(str);
    }
}