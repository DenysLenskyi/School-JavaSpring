package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandDefendant;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolderBuilder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.AddStudentCommand;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.PopulateTablesCommand;

import java.util.Scanner;

@Component
@Profile("!test")
public class UserInteraction implements CommandLineRunner {

    private static final String EXIT = "exit";
    private static final String INCORRECT_INPUT = "Incorrect input";
    private final Logger log = LoggerFactory.getLogger(UserInteraction.class);
    private CommandDefendant commandDefendant;
    private CommandHolder commandHolder;
    private PopulateTablesCommand populateTablesCommand;

    @Autowired
    public UserInteraction(CommandDefendant commandDefendant, CommandHolder commandHolder,
                           PopulateTablesCommand populateTablesCommand) {
        this.commandDefendant = commandDefendant;
        this.commandHolder = commandHolder;
        this.populateTablesCommand = populateTablesCommand;
    }

    @Override
    public void run(String... args) {
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
                commandDefendant.getCommandByCode(commandHolder.getCommandName()).execute(commandHolder);
            } catch (Exception e) {
                System.out.println(INCORRECT_INPUT);
                log.error("Unexpected exception happened");
            }
        }
        scanner.close();
    }
}