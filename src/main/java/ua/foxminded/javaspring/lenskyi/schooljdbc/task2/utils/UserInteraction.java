package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandDefendant;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolderBuilder;

import java.util.Scanner;

@Component
public class UserInteraction {

    private static final String EXIT = "exit";
    private static final String INCORRECT_INPUT = "Incorrect input";
    private static CommandDefendant commandDefendant;
    private static CommandHolder commandHolder;

    @Autowired
    public UserInteraction(CommandDefendant commandDefendant, CommandHolder commandHolder) {
        this.commandDefendant = commandDefendant;
        this.commandHolder = commandHolder;
    }

    public static void runApp() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print('>');
            String userInput = scanner.nextLine();
            if (EXIT.equals(userInput)) {
                break;
            }
            try {
                commandHolder = CommandHolderBuilder.buildCommandFromInputString(userInput);
                commandDefendant.getCommandByCode(commandHolder.getCommandName()).execute(commandHolder);
            } catch (Exception e) {
                System.out.println(INCORRECT_INPUT);
            }
        }
        scanner.close();
    }
}