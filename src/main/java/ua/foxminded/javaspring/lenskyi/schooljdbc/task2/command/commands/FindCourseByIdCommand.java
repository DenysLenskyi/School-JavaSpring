package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.CourseRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;

import java.util.NoSuchElementException;

@Component
@Transactional
public class FindCourseByIdCommand implements Command {

    private static final String COURSE_ID = "Course ID: ";
    private static final String COURSE_NAME = "Course name: ";
    private static final String COURSE_DESCRIPTION = "Description: ";
    private static final String FORMAT = "%1$s %2$s | %3$s %4$s | %5$s %6$s\n";
    private static final String MIN_COURSE_ID = "Minimal course id - ";
    private static final String MAX_COURSE_ID = "Maximal course id - ";
    private static final String FORMAT_FOR_ERROR_MESSAGE = "%1$s %2$s\n%3$s %4$s";

    private CourseRepository courseRepository;

    @Autowired
    public FindCourseByIdCommand(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        try {
            Course course = courseRepository.findById(commandHolder.getCourseId()).orElseThrow();
            String str = String.format(
                    FORMAT, COURSE_ID, course.getId(), COURSE_NAME, course.getName(),
                    COURSE_DESCRIPTION, course.getDescription());
            System.out.println(str);
        } catch (NoSuchElementException e) {
            System.out.println(String.format(FORMAT_FOR_ERROR_MESSAGE,
                    MIN_COURSE_ID, courseRepository.getMinCourseId(),
                    MAX_COURSE_ID, courseRepository.getMaxCourseId()));
        }
    }
}