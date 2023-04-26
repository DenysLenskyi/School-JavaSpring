package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

@Component
public class DeleteStudentCommand implements Command {

    private static final String STUDENT_DOES_NOT_EXIST = "Can't find this student id...";
    private static final String STUDENT_DELETED = "Student deleted";
    private static final String STUDENT_NOT_DELETED = "Student not deleted, check your input";

    private JdbcStudentDao jdbcStudentDao;

    @Autowired
    public DeleteStudentCommand(JdbcStudentDao jdbcStudentDao) {
        this.jdbcStudentDao = jdbcStudentDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        try {
            if (jdbcStudentDao.isStudentExists(commandHolder.getStudentId())) {
                jdbcStudentDao.deleteStudent(commandHolder.getStudentId());
                System.out.println(STUDENT_DELETED);
            } else {
                System.out.println(STUDENT_DOES_NOT_EXIST);
            }
        } catch (Exception e) {
            System.out.println(STUDENT_NOT_DELETED);
        }
    }
}