package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;

@Component
public class UnknownCommand implements Command {

    private static final String UNKNOWN_COMMAND = "Unknown command...";

    @Override
    public void execute(CommandHolder ch) {
        System.out.println(UNKNOWN_COMMAND);
    }
}