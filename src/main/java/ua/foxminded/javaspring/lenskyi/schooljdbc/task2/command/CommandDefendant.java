package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.*;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommandDefendant {

    private static final String INFO = "info";
    private static final String FIND_COURSE_BY_ID = "find_course";

    private Map<String, Command> commandCode = new HashMap<>();
    private InfoCommand infoCommand;
    private UnknownCommand unknownCommand;
    private FindCourseByIdCommand findCourseByIdCommand;


    @Autowired
    public CommandDefendant(
            InfoCommand infoCommand,
            UnknownCommand unknownCommand,
            FindCourseByIdCommand findCourseByIdCommand) {
        this.infoCommand = infoCommand;
        this.unknownCommand = unknownCommand;
        this.findCourseByIdCommand = findCourseByIdCommand;
    }

    public Command getCommandByCode(String code) {
        setCommandCode();
        if (commandCode.containsKey(code)) {
            return commandCode.get(code);
        } else {
            return unknownCommand;
        }
    }

    private void setCommandCode() {
        commandCode.put(INFO, infoCommand);
        commandCode.put(FIND_COURSE_BY_ID, findCourseByIdCommand);
    }
}