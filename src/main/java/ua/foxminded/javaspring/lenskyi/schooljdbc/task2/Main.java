package ua.foxminded.javaspring.lenskyi.schooljdbc.task2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.UserInteraction;

import java.util.Scanner;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        UserInteraction.runApp();
    }
}