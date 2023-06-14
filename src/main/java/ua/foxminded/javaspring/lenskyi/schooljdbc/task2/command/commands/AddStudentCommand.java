package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.GroupRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.StudentRepository;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;

@Component
@Transactional
public class AddStudentCommand implements Command {

    private static final String STUDENT_ADDED = "Student added";
    private static final String STUDENT_NOT_ADDED = "Student not added, check your input";
    private static final String INCORRECT_GROUP_ID = "Incorrect group_id, check info";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private StudentRepository studentRepository;
    private GroupRepository groupRepository;

    @Autowired
    public AddStudentCommand(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        Long maxGroupId = groupRepository.getMaxGroupId();
        Long minGroupId = groupRepository.getMinGroupId();
        if (commandHolder.getGroupId() == 0) {
            Student newStudent = new Student();
            newStudent.setGroupId(null);
            newStudent.setFirstName(commandHolder.getStudentFirstName());
            newStudent.setLastName(commandHolder.getStudentLastName());
            studentRepository.save(newStudent);
            System.out.println(STUDENT_ADDED);
            log.info("Student {} {} added with null group id",
                    commandHolder.getStudentFirstName(), commandHolder.getStudentLastName());
        } else if ((commandHolder.getGroupId() > maxGroupId) || (commandHolder.getGroupId() < minGroupId)) {
            System.out.println(INCORRECT_GROUP_ID);
            System.out.println(STUDENT_NOT_ADDED);
            log.warn("Student not added. Reason: wrong group id - {}", commandHolder.getGroupId());
        } else {
            Student newStudent = new Student();
            newStudent.setGroupId(commandHolder.getGroupId());
            newStudent.setFirstName(commandHolder.getStudentFirstName());
            newStudent.setLastName(commandHolder.getStudentLastName());
            studentRepository.save(newStudent);
            System.out.println(STUDENT_ADDED);
            log.info("Student {} {} added", commandHolder.getStudentFirstName(), commandHolder.getStudentLastName());
        }
    }
}