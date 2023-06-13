package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandCorrelation;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolderBuilder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.PopulateTablesCommand;

import javax.sql.DataSource;
import java.util.Scanner;

@Component
@Profile("!test")
public class UserInteraction implements CommandLineRunner {

    private static final String EXIT = "exit";
    private static final String INCORRECT_INPUT = "Incorrect input data";
    private static final String CAN_NOT_BUILD_COMMAND_FROM_INPUT = "Can't build command, check your input";
    private final Logger log = LoggerFactory.getLogger(UserInteraction.class);
    private CommandCorrelation commandCorrelation;
    private CommandHolder commandHolder;
    private PopulateTablesCommand populateTablesCommand;
    @Autowired
    private DataSource dataSource;

    @Autowired
    public UserInteraction(CommandCorrelation commandCorrelation, CommandHolder commandHolder,
                           PopulateTablesCommand populateTablesCommand) {
        this.commandCorrelation = commandCorrelation;
        this.commandHolder = commandHolder;
        this.populateTablesCommand = populateTablesCommand;
    }

    @Override
    public void run(String... args) {
        migrateWithSqlAndJavaCallbacks();
        populateTablesCommand.execute(commandHolder);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            System.out.print('>');
            if (EXIT.equals(userInput)) {
                break;
            }
            try {
                commandHolder = CommandHolderBuilder.buildCommandFromInputString(userInput);
            } catch (Exception e) {
                log.error(e.getMessage());
                System.out.println(CAN_NOT_BUILD_COMMAND_FROM_INPUT);
            }
            try {
                executeCommand(commandHolder);
            } catch (Exception e) {
                System.out.println(INCORRECT_INPUT);
                log.error(e.getMessage());
            }
        }
        scanner.close();
    }

    @Transactional
    void migrateWithSqlAndJavaCallbacks() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration", "db/callback")
                .callbacks(new ExampleFlywayCallback())
                .load();
        flyway.migrate();
    }

    @Transactional
    void executeCommand(CommandHolder commandHolder) {
        commandCorrelation.getCommandByCode(commandHolder.getCommandName()).execute(commandHolder);
    }
}