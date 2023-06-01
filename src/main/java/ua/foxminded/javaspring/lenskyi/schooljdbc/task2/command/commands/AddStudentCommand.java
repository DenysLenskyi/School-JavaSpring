package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentDao;

@Component
public class AddStudentCommand implements Command {

    private static final String STUDENT_ADDED = "Student added";
    private static final String STUDENT_NOT_ADDED = "Student not added, check your input";
    private static final String INCORRECT_GROUP_ID = "Incorrect group_id, check info";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private JpaStudentDao jpaStudentDao;
    private int maxGroupId = 10;

    @Autowired
    public AddStudentCommand(JpaStudentDao jpaStudentDao) {
        this.jpaStudentDao = jpaStudentDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        if ((commandHolder.getGroupId() > maxGroupId) || (commandHolder.getGroupId() < 0)) {
            System.out.println(INCORRECT_GROUP_ID);
            System.out.println(STUDENT_NOT_ADDED);
            log.warn("Student not added. Reason: wrong group id - {}", commandHolder.getGroupId());
        } else {
            jpaStudentDao.addStudent(commandHolder.getGroupId(),
                    commandHolder.getStudentFirstName(), commandHolder.getStudentLastName());
            System.out.println(STUDENT_ADDED);
            log.info("Student {} {} added", commandHolder.getStudentFirstName(), commandHolder.getStudentLastName());
        }
    }
}