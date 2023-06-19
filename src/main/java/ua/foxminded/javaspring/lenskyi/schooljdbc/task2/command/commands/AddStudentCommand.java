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

import java.util.ArrayList;
import java.util.List;

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
        List<Integer> availableGroupIdList = getAvailableGroupIdList();
        if (availableGroupIdList.contains(Math.toIntExact(commandHolder.getGroupId()))) {
            Student newStudent = new Student();
            if (commandHolder.getGroupId() == 0) {
                newStudent.setGroupId(null);
            } else {
                newStudent.setGroupId(commandHolder.getGroupId());
            }
            newStudent.setFirstName(commandHolder.getStudentFirstName());
            newStudent.setLastName(commandHolder.getStudentLastName());
            studentRepository.save(newStudent);
            System.out.println(STUDENT_ADDED);
            log.info("Student {} {} added",
                    commandHolder.getStudentFirstName(), commandHolder.getStudentLastName());
        } else {
            System.out.println(INCORRECT_GROUP_ID);
            System.out.println(STUDENT_NOT_ADDED);
            log.warn("Student not added. Reason: wrong group id - {}", commandHolder.getGroupId());
        }
    }

    private List<Integer> getAvailableGroupIdList() {
        List<Integer> availableGroupIds = new ArrayList<>();
        for (int i = Math.toIntExact(groupRepository.getMinGroupId()); i <= groupRepository.getMaxGroupId(); i++) {
            availableGroupIds.add(i);
        }
        availableGroupIds.add(0);
        return availableGroupIds;
    }
}