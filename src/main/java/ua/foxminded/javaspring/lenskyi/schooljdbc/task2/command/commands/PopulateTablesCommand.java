package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.transaction.Transactional;
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
@Transactional
public class PopulateTablesCommand implements Command {

    private JpaCourseDao jpaCourseDao;
    private JpaGroupDao jpaGroupDao;
    private JpaStudentDao jpaStudentDao;
    private JpaStudentCourseDao jpaStudentCourseDao;
    private RandomDataCreator randomDataCreator;
    private int numGroups = 10;
    private int numStudents = 200;

    @Autowired
    public PopulateTablesCommand(JpaCourseDao jpaCourseDao, JpaGroupDao jpaGroupDao,
                                 JpaStudentDao jpaStudentDao, JpaStudentCourseDao jpaStudentCourseDao,
                                 RandomDataCreator randomDataCreator) {
        this.jpaCourseDao = jpaCourseDao;
        this.jpaGroupDao = jpaGroupDao;
        this.jpaStudentDao = jpaStudentDao;
        this.jpaStudentCourseDao = jpaStudentCourseDao;
        this.randomDataCreator = randomDataCreator;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        jpaCourseDao.addCourses(randomDataCreator.getCoursesFromResources());
        jpaGroupDao.addGroups(randomDataCreator.generateGroups(numGroups));
        jpaStudentDao.addStudents(randomDataCreator.generateStudents(numStudents));
        jpaStudentCourseDao.addStudentsCourses(randomDataCreator.enrollStudentsToCourses());
    }
}