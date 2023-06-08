package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaStudentDao;

@Component
@Transactional
public class DeleteStudentCommand implements Command {

    private static final String STUDENT_DOES_NOT_EXIST = "Can't find this student id...";
    private static final String STUDENT_DELETED = "Student deleted";
    private static final String STUDENT_NOT_DELETED = "Student not deleted, check your input";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private JpaStudentDao jpaStudentDao;

    @Autowired
    public DeleteStudentCommand(JpaStudentDao jpaStudentDao) {
        this.jpaStudentDao = jpaStudentDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        try {
            if (jpaStudentDao.doesStudentExist(commandHolder.getStudentId())) {
                jpaStudentDao.deleteStudent(commandHolder.getStudentId());
                System.out.println(STUDENT_DELETED);
                log.info("Student with id={} deleted", commandHolder.getStudentId());
            } else {
                System.out.println(STUDENT_DOES_NOT_EXIST);
                log.warn("Student id={} doesn't exist. Can't delete", commandHolder.getStudentId());
            }
        } catch (Exception e) {
            System.out.println(STUDENT_NOT_DELETED);
            log.error(e.getMessage());
        }
    }
}