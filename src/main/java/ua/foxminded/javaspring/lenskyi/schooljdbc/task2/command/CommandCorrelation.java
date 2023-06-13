package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.*;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommandCorrelation {

    private static final String INFO = "info";
    private static final String FIND_COURSE_BY_ID = "find_course";
    private static final String FIND_GROUPS = "find_groups";
    private static final String FIND_STUDENTS_COURSE = "find_students_course";
    private static final String ADD_STUDENT = "add_student";
    private static final String DELETE_STUDENT = "delete_student";
    private static final String ADD_STUDENT_COURSE = "add_student_course";
    private static final String DELETE_STUDENT_COURSE = "delete_student_course";

    private Map<String, Command> commandCode = new HashMap<>();
    private InfoCommand infoCommand;
    private UnknownCommand unknownCommand;
    private FindCourseByIdCommand findCourseByIdCommand;
    private FindGroupsWithNumStudentsCommand findGroupsWithNumStudentsCommand;
    private FindStudentsEnrolledToCourseCommand findStudentsEnrolledToCourseCommand;
    private AddStudentCommand addStudentCommand;
    private DeleteStudentCommand deleteStudentCommand;
    private EnrollStudentToCourseCommand enrollStudentToCourseCommand;
    private RemoveStudentFromCourseCommand removeStudentFromCourseCommand;

    @Autowired
    public CommandCorrelation(InfoCommand infoCommand,
                              UnknownCommand unknownCommand,
                              FindCourseByIdCommand findCourseByIdCommand,
                              FindGroupsWithNumStudentsCommand findGroupsWithNumStudentsCommand,
                              FindStudentsEnrolledToCourseCommand findStudentsEnrolledToCourseCommand,
                              AddStudentCommand addStudentCommand, DeleteStudentCommand deleteStudentCommand,
                              EnrollStudentToCourseCommand enrollStudentToCourseCommand,
                              RemoveStudentFromCourseCommand removeStudentFromCourseCommand) {
        this.infoCommand = infoCommand;
        this.unknownCommand = unknownCommand;
        this.findCourseByIdCommand = findCourseByIdCommand;
        this.findGroupsWithNumStudentsCommand = findGroupsWithNumStudentsCommand;
        this.findStudentsEnrolledToCourseCommand = findStudentsEnrolledToCourseCommand;
        this.addStudentCommand = addStudentCommand;
        this.deleteStudentCommand = deleteStudentCommand;
        this.enrollStudentToCourseCommand = enrollStudentToCourseCommand;
        this.removeStudentFromCourseCommand = removeStudentFromCourseCommand;
    }

    public Map<String, Command> getCommandCode() {
        return commandCode;
    }

    public Command getCommandByCode(String code) {
        if (getCommandCode().isEmpty()) {
            setCommandCode();
        }
        if (commandCode.containsKey(code)) {
            return commandCode.get(code);
        } else {
            return unknownCommand;
        }
    }

    private void setCommandCode() {
        commandCode.put(INFO, infoCommand);
        commandCode.put(FIND_COURSE_BY_ID, findCourseByIdCommand);
        commandCode.put(FIND_GROUPS, findGroupsWithNumStudentsCommand);
        commandCode.put(FIND_STUDENTS_COURSE, findStudentsEnrolledToCourseCommand);
        commandCode.put(ADD_STUDENT, addStudentCommand);
        commandCode.put(DELETE_STUDENT, deleteStudentCommand);
        commandCode.put(ADD_STUDENT_COURSE, enrollStudentToCourseCommand);
        commandCode.put(DELETE_STUDENT_COURSE, removeStudentFromCourseCommand);
    }
}