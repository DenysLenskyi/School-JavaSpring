package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JpaGroupDao;

@Component
public class FindGroupsWithNumStudentsCommand implements Command {

    private static final String DISCLAIMER = "Groups with less or equal than";
    private static final String STUDENTS = "students";
    private static final String GROUP_ID = "Group ID: ";
    private static final String GROUP_NAME = "Group name: ";
    private static final String FORMAT = "%1$s %2$s | %3$s %4$s";
    private static final String DISCLAIMER_FORMAT = "%1$s %2$s %3$s";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private JpaGroupDao jpaGroupDao;

    @Autowired
    public FindGroupsWithNumStudentsCommand(JpaGroupDao jpaGroupDao) {
        this.jpaGroupDao = jpaGroupDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        System.out.println(String.format(DISCLAIMER_FORMAT, DISCLAIMER, commandHolder.getNumStudents(), STUDENTS));
        jpaGroupDao.findGroupsWithNumStudents(commandHolder.getNumStudents()).stream()
                .map(group -> String.format(FORMAT, GROUP_ID, group.getId(), GROUP_NAME, group.getName()))
                .forEach(System.out::println);
        System.out.println('\n');
        log.info("FindGroupsWithNumStudentsCommand executed");
    }
}