package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.CourseRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.StudentRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;

@Component
@Transactional
public class EnrollStudentToCourseCommand implements Command {

    private static final String STUDENT_ENROLLED_TO_COURSE = "Student enrolled to the course";
    private static final String WRONG_STUDENT_ID = "Wrong student's id. This id doesn't exist in the database";
    private static final String STUDENT_ALREADY_ENROLLED = "This student already visits this course";
    private static final String WRONG_COURSE_NAME = """
            Wrong course name.
            Available courses: Math, English, Biologic, Geography, Chemistry,
                               Physics, History, Finance, Sports, Etiquette.
            """;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private StudentRepository studentRepository;
    private CourseRepository courseRepository;

    @Autowired
    public EnrollStudentToCourseCommand(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        if (!(studentRepository.existsById(commandHolder.getStudentId()))) {
            System.out.println(WRONG_STUDENT_ID);
            log.warn("Attempt to enroll non existed student with id={} to a course", commandHolder.getStudentId());
            return;
        }
        if (!(courseRepository.existsByName(commandHolder.getCourseName()))) {
            System.out.println(WRONG_COURSE_NAME);
            log.warn("Attempt to enroll student to non existed course with name = {}", commandHolder.getCourseName());
            return;
        }
        if (studentRepository.doesStudentVisitTheCourse(commandHolder.getStudentId(),
                courseRepository.findCourseByName(commandHolder.getCourseName()).getId())) {
            System.out.println(STUDENT_ALREADY_ENROLLED);
            log.warn("Attempt to enroll already enrolled student to the course");
        } else {
            Student student = studentRepository.findById(commandHolder.getStudentId()).orElseThrow();
            student.addCourse(courseRepository.findCourseByName(commandHolder.getCourseName()));
            studentRepository.save(student);
            System.out.println(STUDENT_ENROLLED_TO_COURSE);
            log.info("Student with id={} enrolled to the course {}", commandHolder.getStudentId(),
                    commandHolder.getCourseName());
        }
    }
}