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
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.SchoolCache;

@Component
public class PopulateTablesCommand implements Command {

    private JpaCourseDao jpaCourseDao;
    private JpaGroupDao jpaGroupDao;
    private JpaStudentDao jpaStudentDao;
    private JpaStudentCourseDao jpaStudentCourseDao;
    private RandomDataCreator randomDataCreator;
    private SchoolCache schoolCache;
    private int numGroups = 10;
    private int numStudents = 200;

    @Autowired
    public PopulateTablesCommand(JpaCourseDao jpaCourseDao, JpaGroupDao jpaGroupDao,
                                 JpaStudentDao jpaStudentDao, JpaStudentCourseDao jpaStudentCourseDao,
                                 RandomDataCreator randomDataCreator, SchoolCache schoolCache) {
        this.jpaCourseDao = jpaCourseDao;
        this.jpaGroupDao = jpaGroupDao;
        this.jpaStudentDao = jpaStudentDao;
        this.jpaStudentCourseDao = jpaStudentCourseDao;
        this.randomDataCreator = randomDataCreator;
        this.schoolCache = schoolCache;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        jpaCourseDao.addCourses(randomDataCreator.getCoursesFromResources());
        schoolCache.setMinCourseId(jpaCourseDao.getMinCourseId());
        schoolCache.setMaxCourseId(jpaCourseDao.getMaxCourseId());
        jpaGroupDao.addGroups(randomDataCreator.generateGroups(numGroups));
        schoolCache.setMinGroupId(jpaGroupDao.getMinGroupId());
        schoolCache.setMaxGroupId(jpaGroupDao.getMaxGroupId());
        jpaStudentDao.addStudents(randomDataCreator.generateStudents(numStudents));
        schoolCache.setMinStudentId(jpaStudentDao.getMinStudentId());
        schoolCache.setMaxStudentId(jpaStudentDao.getMaxStudentId());
        jpaStudentCourseDao.addStudentsCourses(randomDataCreator.enrollStudentsToCourses());
    }
}