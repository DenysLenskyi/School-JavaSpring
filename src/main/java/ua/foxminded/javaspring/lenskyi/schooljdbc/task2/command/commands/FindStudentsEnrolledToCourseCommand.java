package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.CourseRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.StudentCourseRepository;

@Component
@Transactional
public class FindStudentsEnrolledToCourseCommand implements Command {

    private static final String STUDENT_ID = "Student ID:";
    private static final String STUDENT_FULL_NAME = "Student name:";
    private static final String FORMAT = "%1$s %2$s | %3$s %4$s %5$s";
    private static final String DISCLAIMER_AFTER_WRONG_INPUT = """
            Failed...
            Available courses: Math, English, Biologic, Geography, Chemistry,
                               Physics, History, Finance, Sports, Etiquette.
            """;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private StudentCourseRepository jdbcStudentCoursesDao;
    private CourseRepository jdbcCourseDao;

    @Autowired
    public FindStudentsEnrolledToCourseCommand(StudentCourseRepository jdbcStudentCoursesDao, CourseRepository jdbcCourseDao) {
        this.jdbcStudentCoursesDao = jdbcStudentCoursesDao;
        this.jdbcCourseDao = jdbcCourseDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        if (jdbcCourseDao.existsByName(commandHolder.getCourseName())) {
            jdbcStudentCoursesDao.getStudentsEnrolledToCourse(commandHolder.getCourseName())
                    .stream()
                    .map(student -> String.format(FORMAT, STUDENT_ID, student.getId(),
                            STUDENT_FULL_NAME, student.getFirstName(), student.getLastName()))
                    .forEach(System.out::println);
            System.out.println('\n');
            log.info("Command executed");
        } else {
            System.out.println(DISCLAIMER_AFTER_WRONG_INPUT);
            log.warn("Course with name {} not present in database", commandHolder.getCourseName());
        }
    }
}