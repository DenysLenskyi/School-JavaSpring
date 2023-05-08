package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcGroupDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.RandomDataCreator;

@Component
public class PopulateTablesCommand implements Command {

    private JdbcCourseDao jdbcCourseDao;
    private JdbcGroupDao jdbcGroupDao;
    private JdbcStudentDao jdbcStudentDao;
    private JdbcStudentCourseDao jdbcStudentCoursesDao;
    private RandomDataCreator randomDataCreator;
    private int numGroups = 10;
    private int numStudents = 200;

    @Autowired
    public PopulateTablesCommand(JdbcCourseDao jdbcCourseDao, JdbcGroupDao jdbcGroupDao,
                                 JdbcStudentDao jdbcStudentDao, JdbcStudentCourseDao jdbcStudentCoursesDao,
                                 RandomDataCreator randomDataCreator) {
        this.jdbcCourseDao = jdbcCourseDao;
        this.jdbcGroupDao = jdbcGroupDao;
        this.jdbcStudentDao = jdbcStudentDao;
        this.jdbcStudentCoursesDao = jdbcStudentCoursesDao;
        this.randomDataCreator = randomDataCreator;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        jdbcCourseDao.addCourses(randomDataCreator.getCoursesFromResources());
        jdbcGroupDao.addGroups(randomDataCreator.generateGroups(numGroups));
        jdbcStudentDao.addStudents(randomDataCreator.generateStudents(numStudents));
        jdbcStudentCoursesDao.addStudentsCourses(randomDataCreator.generateStudentCourseRelation(numStudents));
    }
}