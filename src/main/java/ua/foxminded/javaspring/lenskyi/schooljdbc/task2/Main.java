package ua.foxminded.javaspring.lenskyi.schooljdbc.task2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.CreateTablesCommand;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.PopulateTablesCommand;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.UserInteraction;

@SpringBootApplication
public class Main {

    private static PopulateTablesCommand populateTablesCommand;
    private static CreateTablesCommand createTablesCommand;

    @Autowired
    public Main(PopulateTablesCommand populateTablesCommand, CreateTablesCommand createTablesCommand) {
        this.populateTablesCommand = populateTablesCommand;
        this.createTablesCommand = createTablesCommand;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        createTables();
        populateTables();
        UserInteraction.runApp();
    }

    private static void populateTables() {
        populateTablesCommand.execute(new CommandHolder());
    }

    private static void createTables() {
        createTablesCommand.execute(new CommandHolder());
    }
}