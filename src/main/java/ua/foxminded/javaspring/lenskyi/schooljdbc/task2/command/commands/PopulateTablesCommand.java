package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.beans.factory.annotation.Autowired;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.RandomDataCreator;

public class PopulateTablesCommand implements Command {

    private JdbcCourseDao jdbcCourseDao;
    private RandomDataCreator randomDataCreator;

    @Autowired
    public PopulateTablesCommand(JdbcCourseDao jdbcCourseDao, RandomDataCreator randomDataCreator) {
        this.jdbcCourseDao = jdbcCourseDao;
        this.randomDataCreator = randomDataCreator;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        jdbcCourseDao.addCourses(randomDataCreator.getCoursesFromResources());
    }
}