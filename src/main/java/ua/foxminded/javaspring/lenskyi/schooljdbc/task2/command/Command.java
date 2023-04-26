package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command;

public interface Command {

    void execute(CommandHolder ch);
}