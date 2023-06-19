package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.CourseRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.GroupRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.StudentRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.RandomDataCreator;

@Component
@Transactional
public class PopulateTablesCommand implements Command {

    private CourseRepository courseRepository;
    private GroupRepository groupRepository;
    private StudentRepository studentRepository;
    private RandomDataCreator randomDataCreator;
    private int numGroups = 10;
    private int numStudents = 200;

    @Autowired
    public PopulateTablesCommand(CourseRepository courseRepository, GroupRepository groupRepository,
                                 StudentRepository studentRepository, RandomDataCreator randomDataCreator) {
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.randomDataCreator = randomDataCreator;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        courseRepository.saveAll(randomDataCreator.getCoursesFromResources());
        groupRepository.saveAll(randomDataCreator.generateGroups(numGroups));
        studentRepository.saveAll(randomDataCreator.generateStudents(numStudents));
    }
}