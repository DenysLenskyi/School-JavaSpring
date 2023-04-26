package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

@Component
public class AddStudentCommand implements Command {

    private static final String STUDENT_ADDED = "Student added";
    private static final String STUDENT_NOT_ADDED = "Student not added, check your input";
    private static final String INCORRECT_GROUP_ID = "Group id could be in range from 0 to 10";

    private JdbcStudentDao jdbcStudentDao;
    private int maxGroupId = 10;

    @Autowired
    public AddStudentCommand(JdbcStudentDao jdbcStudentDao) {
        this.jdbcStudentDao = jdbcStudentDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        try {
            jdbcStudentDao.addStudent(commandHolder.getGroupId(),
                    commandHolder.getStudentFirstName(), commandHolder.getStudentLastName());
            System.out.println(STUDENT_ADDED);
        } catch (Exception e) {
            System.out.println(STUDENT_NOT_ADDED);
        }
        if ((commandHolder.getGroupId() > maxGroupId) || (commandHolder.getGroupId() < 0)) {
            System.out.println(INCORRECT_GROUP_ID);
        }
    }
}