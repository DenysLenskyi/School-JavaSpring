package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaGroupDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.RandomDataCreator;

@Component
public class PopulateTablesCommand implements Command {

    private JpaCourseDao jdbcCourseDao;
    private JpaGroupDao jpaGroupDao;
    private JpaStudentDao jpaStudentDao;
    private JpaStudentCourseDao jdbcStudentCoursesDao;
    private RandomDataCreator randomDataCreator;
    private int numGroups = 10;
    private int numStudents = 200;

    @Autowired
    public PopulateTablesCommand(JpaCourseDao jdbcCourseDao, JpaGroupDao jpaGroupDao,
                                 JpaStudentDao jpaStudentDao, JpaStudentCourseDao jdbcStudentCoursesDao,
                                 RandomDataCreator randomDataCreator) {
        this.jdbcCourseDao = jdbcCourseDao;
        this.jpaGroupDao = jpaGroupDao;
        this.jpaStudentDao = jpaStudentDao;
        this.jdbcStudentCoursesDao = jdbcStudentCoursesDao;
        this.randomDataCreator = randomDataCreator;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        jdbcCourseDao.addCourses(randomDataCreator.getCoursesFromResources());
        jpaGroupDao.addGroups(randomDataCreator.generateGroups(numGroups));
        jpaStudentDao.addStudents(randomDataCreator.generateStudents(numStudents));
        jdbcStudentCoursesDao.addStudentsCourses(randomDataCreator.enrollStudentsToCourses());
    }
}